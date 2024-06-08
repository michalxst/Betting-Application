package betsapp.air.betsapp.BetProcedure;

import betsapp.air.betsapp.BetProcedure.historical.BetScoreService;
import betsapp.air.betsapp.model.api.upcoming.UpcomingGameData;
import betsapp.air.betsapp.user.GetUser;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Controller
@RequestMapping("/")
@EnableScheduling
@AllArgsConstructor
public class HomePage {
    private final UserInfoRepository userInfoRepository;
    private final BetScoreService betScoreService;
    private final GetUser getUser;

    @Scheduled(fixedRate = 5* 60000)
    public void updateBetStatusPeriodically() {
        betScoreService.fetchAndUpdateBetStatus();
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfo> getUserInfo() {
        Integer userId = getUser.getUserByID();
        if (userId != null) {
            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            if (userInfo != null) {
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public String showData(Model model) {
        Integer userId = getUser.getUserByID();
        if (userId != null) {
            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            model.addAttribute("userInfo", userInfo);
        }

        String jsonResponse;
        Gson gson = new Gson();
        String region = "eu";
        String bookmakers = "unibet";
        String apiKey = "bcb3cb7c1694661949376885fef2e814";
        String sportType ="basketball_nba";

        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.the-odds-api.com/v4/sports/" + sportType + "/odds/?regions=" + region + "&markets=h2h&bookmakers=" + bookmakers + "&apiKey=" + apiKey))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            jsonResponse = getResponse.body();

            if (getResponse.statusCode() == 200) {
                List<UpcomingGameData> upcomingGames = gson.fromJson(jsonResponse, new TypeToken<List<UpcomingGameData>>() {}.getType());
                model.addAttribute("upcomingGames", upcomingGames);
                return "oddsTable";
            } else {
                System.err.println("JSON response error");
                return "error-page";
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
