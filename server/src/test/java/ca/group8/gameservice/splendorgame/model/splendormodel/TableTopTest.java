//package ca.group8.gameservice.splendorgame.model.splendormodel;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TableTopTest {
//
//    TableTop t1;
//    ArrayList<PlayerInGame> activePlayerInGames = new ArrayList<PlayerInGame>();
//    @BeforeEach
//    void setup() {
//        PlayerInGame p1 = new PlayerInGame("P1");
//        PlayerInGame p2 = new PlayerInGame("P2");
//        PlayerInGame p3 = new PlayerInGame("P3");
//        activePlayerInGames.add(p1);
//        activePlayerInGames.add(p2);
//        activePlayerInGames.add(p3);
//        t1 = new TableTop(activePlayerInGames);
//    }
//
//
//    @Test
//    void testIsNew(){
//        for(PlayerInGame player :activePlayerInGames){
//            //assert(player.isEmpty());
//        }
//        for(int i=0; i<3; i++){
//            for(int j=0; j<4; j++){
//                assert(t1.getBaseBoard().getCard(i,j) instanceof DevelopmentCard);
//            }
//        }
//        for(int i = 0; i < activePlayerInGames.size() +1; i++){
//            assert(t1.getNobleBoard().getCard(i,0) instanceof NobleCard);
//        }
//        assert(t1.getDecks().size()==3);
//        assert(t1.getDecks().get(1).size()==36);
//    }
//
//
//}