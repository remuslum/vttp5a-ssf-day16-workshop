package sg.nus.edu.iss.vttp5a_day16_workshop.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.nus.edu.iss.vttp5a_day16_workshop.model.BoardGame;
import sg.nus.edu.iss.vttp5a_day16_workshop.service.BoardGameRestService;
import sg.nus.edu.iss.vttp5a_day16_workshop.service.ListService;
import sg.nus.edu.iss.vttp5a_day16_workshop.util.MyConstants;

@RestController
@RequestMapping("/api")
public class BoardGameRestController {
    
    @Autowired
    BoardGameRestService boardGameRestService;

    @Autowired
    ListService listService;

    private int count = 0;

    @GetMapping()
    public ResponseEntity<List<BoardGame>> getBoardGames() throws IOException {
        List<BoardGame> boardGameList = boardGameRestService.getBoardGame();
        return ResponseEntity.ok().body(boardGameList);
    }

    @PostMapping("/boardgame")
    public ResponseEntity<String> loadBoardGames() throws IOException {
        List<BoardGame> boardGameList = boardGameRestService.getBoardGame();
        boardGameList.forEach(x -> listService.addToRedis(MyConstants.REDISKEY, x.toString()));
        JsonObject response = Json.createObjectBuilder().add("insert_count", 1).add("id",MyConstants.REDISKEY).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
    }

    @GetMapping("/boardgame/{boardgame-id}")
    public ResponseEntity<String> retrieveBoardGame(@PathVariable("boardgame-id") String boardGameID){
        List<BoardGame> boardGameList = listService.getBoardGames(MyConstants.REDISKEY);
        Optional<BoardGame> boardGameOptional = boardGameList.stream().filter(x -> x.getGameID().equals(Integer.valueOf(boardGameID))).findFirst();
        if(boardGameOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board game was not found");
        } else {
            BoardGame boardGame = boardGameOptional.get();
            JsonObject response = Json.createObjectBuilder().add("gid", boardGame.getGameID()).add("name", boardGame.getName())
            .add("year", boardGame.getYear()).add("ranking", boardGame.getRanking()).add("users_rated", boardGame.getUsersRated())
            .add("url", boardGame.getUrl()).add("image", boardGame.getImageLink()).build();
            return ResponseEntity.ok().body(response.toString());
        }
    }

    @PutMapping("/boardgame/{boardgame-id}")
    public ResponseEntity<String> updateBoardGame(@PathVariable("boardgame-id") String boardGameID, @RequestBody String entity, @RequestParam(defaultValue = "false") boolean upsert){
        JsonReader jsonReader = Json.createReader(new StringReader(entity));
        JsonObject jObject = jsonReader.readObject();
        
        BoardGame newBoardGame = new BoardGame(jObject.getInt("gid"), jObject.getString("name"), jObject.getInt("year"), 
        jObject.getInt("ranking"), jObject.getInt("users_rated"), jObject.getString("url"), jObject.getString("image"));

        List<BoardGame> boardGameList = listService.getBoardGames(MyConstants.REDISKEY);
        Optional<BoardGame> boardGameOptional = boardGameList.stream().filter(x -> x.getGameID().equals(Integer.valueOf(boardGameID))).findFirst();
        if(boardGameOptional.isEmpty()){
            if(upsert){
                listService.addToRedis(MyConstants.REDISKEY, newBoardGame.toString());
                return ResponseEntity.ok().body("Board Game successfully added");
            }
                return ResponseEntity.status(400).body("Boardgame not found");
        } else {
            listService.removeValueFromList(MyConstants.REDISKEY, boardGameOptional.get());
            listService.addToRedis(MyConstants.REDISKEY, newBoardGame.toString());
            count++;
            JsonObject response = Json.createObjectBuilder().add("update_count", count).add("id",MyConstants.REDISKEY).build();
            return ResponseEntity.ok().body(response.toString());
        }
    }
}
