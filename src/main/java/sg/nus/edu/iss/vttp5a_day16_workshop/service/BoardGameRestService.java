package sg.nus.edu.iss.vttp5a_day16_workshop.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.nus.edu.iss.vttp5a_day16_workshop.model.BoardGame;

@Service
public class BoardGameRestService {
    
    public List<BoardGame> getBoardGame() throws IOException{
        List<BoardGame> boardGameList = new ArrayList<>();

        InputStream fis = new FileInputStream(new File("/Users/remuslum/Downloads/vttp_ssf/vttp5a-day16-workshop/data/game.json"));
        JsonReader jsonReader = Json.createReader(fis);
        JsonArray jsonArray = jsonReader.readArray();

        for(int i = 0; i < jsonArray.size(); i++){
            JsonObject jsonObject = jsonArray.getJsonObject(i);
            BoardGame boardGame = new BoardGame(jsonObject.getInt("gid"), jsonObject.getString("name"), jsonObject.getInt("year"), jsonObject.getInt("ranking"),
            jsonObject.getInt("users_rated"), jsonObject.getString("url"), jsonObject.getString("image"));
            boardGameList.add(boardGame);
        }
        return boardGameList;
    }
}
