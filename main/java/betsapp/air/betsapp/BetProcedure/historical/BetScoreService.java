package betsapp.air.betsapp.BetProcedure.historical;

import betsapp.air.betsapp.BetProcedure.BetData;
import betsapp.air.betsapp.BetProcedure.BetDataRepository;
import betsapp.air.betsapp.user.GetUser;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@AllArgsConstructor
@Service
public class BetScoreService {
    private final BetDataRepository betDataRepository;
    private final UserInfoRepository userInfoRepository;


    public void fetchAndUpdateBetStatus() {
        String jsonResponse = fetchOddsFromApi();
        if (jsonResponse != null) {
            updateBetStatusFromJson(jsonResponse);
        } else {
            System.err.println("Error fetching odds from API");
        }
    }

    private String fetchOddsFromApi() {
        String jsonResponse;
        String apiKey = "bcb3cb7c1694661949376885fef2e814";
        String sportType = "basketball_nba";

        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.the-odds-api.com/v4/sports/" + sportType + "/scores/?apiKey=" + apiKey + "&daysFrom=1"))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            jsonResponse = getResponse.body();

            if (getResponse.statusCode() == 200) {
                return jsonResponse;
            } else {
                System.err.println("JSON response error");
                return null;
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateBetStatusFromJson(String jsonResponse) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonData = objectMapper.readTree(jsonResponse);
            updateBetStatus(jsonData);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }

    private void updateBetStatus(JsonNode jsonData) {

        for (JsonNode game : jsonData) {
            if(game.get("completed").asText().equals("true")){
                String gameId = game.get("id").asText();
                List<BetData> betDataList = betDataRepository.findAllByGameId(gameId);

                if (!betDataList.isEmpty()) {
                    for (BetData betData : betDataList) {
                        boolean gameWon = determineGameResult(game, betData);
                        if (gameWon) {
                            updateUsersCredits(betData);
                            betData.setBetStatus("won");

                        } else {
                            updateOnLoss(betData);
                            betData.setBetStatus("lost");
                        }
                        betDataRepository.save(betData);

                    }
                }
            }
        }
    }

    private void updateUsersCredits(BetData betData){
        if(betData.getBetStatus().equals("active")){
            BigDecimal rate = betData.getBetRate();
            BigDecimal value = betData.getBetValue();
            Integer userID = betData.getUserId();
            UserInfo userInfo = userInfoRepository.findByUserID(userID);
            BigDecimal currentCredits = userInfo.getCredits();
            BigDecimal newCredits = currentCredits.add(rate.multiply(value));
            userInfo.setCredits(newCredits);
            betData.setBetWon(rate.multiply(value));
            userInfoRepository.save(userInfo);
        }
    }

    private void updateOnLoss(BetData betData){
        BigDecimal value = betData.getBetValue();
        betData.setBetWon(value.negate());
    }
    private boolean determineGameResult(JsonNode game, BetData betData) {
        String homeTeam = game.get("home_team").asText();
        String awayTeam = game.get("away_team").asText();
        String draw = "Draw";
        int homeScore = game.get("scores").get(0).get("score").asInt();
        int awayScore = game.get("scores").get(1).get("score").asInt();
        String expectedWinningTeam = betData.getBetExpectedResult();

        return (homeTeam.equals(expectedWinningTeam) && homeScore > awayScore) ||
                (awayTeam.equals(expectedWinningTeam) && awayScore > homeScore) ||
                (draw.equals(expectedWinningTeam) && awayScore == homeScore);
    }

}
