package betsapp.air.betsapp.event;

import betsapp.air.betsapp.user.UserInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;



@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private UserInfo userInfo;
    private String applicationUrl;

    public RegistrationCompleteEvent(UserInfo userInfo, String applicationUrl) {
        super(userInfo);
        this.userInfo = userInfo;
        this.applicationUrl = applicationUrl;
    }

}
