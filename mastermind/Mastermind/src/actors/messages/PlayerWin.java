package actors.messages;

import info.PlayerInfo;

public class PlayerWin implements Message{

    private PlayerInfo player;

    public PlayerWin(PlayerInfo player){

        this.player = player;
    }

    public PlayerInfo getPlayerWinn(){
        return this.player;
    }
}
