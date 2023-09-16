package ru.msvdev.homefinance.window;

import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Getter
@Component
public class MainAppStage {

    private Stage stage;

    public void setStage(Stage stage) {
        if (this.stage != null) {
            throw new RuntimeException("Нельзя переопределять Stage, созданный JavaFx в момент старта приложения");
        }
        this.stage = stage;
    }
}
