package betsapp.air.betsapp;

import betsapp.air.betsapp.BetProcedure.historical.BetScoreService;
import betsapp.air.betsapp.model.api.upcoming.UpcomingGameData;
import betsapp.air.betsapp.user.GetUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import betsapp.air.betsapp.user.UserInfo;
import betsapp.air.betsapp.user.UserInfoRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication

public class BetsappApplication {
	public static void main(String[] args) {
		SpringApplication.run(BetsappApplication.class, args);
	}
}
