package sg.nus.edu.iss.vttp5a_day16_workshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.nus.edu.iss.vttp5a_day16_workshop.model.BoardGame;
import sg.nus.edu.iss.vttp5a_day16_workshop.repo.ListRepo;

@Service
public class ListService {
    
    @Autowired
    ListRepo listRepo;

    public void addToRedis(String key, String value){
        listRepo.rightPush(key, value);
    }

    public String getElementFromRedis(String key, Integer index){
        return listRepo.get(key, index);
    }

    public List<String> getListFromRedis(String key){
        return listRepo.getList(key);
    }

    public List<BoardGame> getBoardGames(String key){
        List<String> redisList = listRepo.getList(key);
        List<String[]> redisItems = new ArrayList<>();
        for(String item:redisList){
            redisItems.add(item.split(";"));
        }

        List<BoardGame> boardGames = new ArrayList<>();
        for(String[] boardGameString: redisItems){
            Map<String, String> mapValue = new HashMap<>();
            for(String element:boardGameString){
                String[] temp = element.split("/=");
                mapValue.put(temp[0], temp[1]);
            }
            boardGames.add(new BoardGame(Integer.valueOf(mapValue.get("gid")), mapValue.get("name"), Integer.valueOf(mapValue.get("year")), Integer.valueOf(mapValue.get("ranking")), Integer.valueOf(mapValue.get("usersRated")), mapValue.get("url"), mapValue.get("imageLink")));
        }
        return boardGames;
    }

    public void removeValueFromList(String key, BoardGame boardGame){
        listRepo.removeValueFromList(key, boardGame);
    }
}
