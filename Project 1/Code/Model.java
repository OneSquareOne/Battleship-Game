public class Model {
    Controller gameController;
    
    private Player thisPlayer;
	private Player opponentShadow; 
	private State thisPlayerState;
    private Role thisPlayerRole;
    static final int BOARD_ROWS = 10; // can be changed for larger board size
    static final int BOARD_COLS = 10;

    public Model(Controller controller){
        gameController = controller;
        gameController.registerModel(this);
        thisPlayer = new Player("Player", BOARD_ROWS, BOARD_COLS);
        opponentShadow = new Player("Opponent", BOARD_ROWS, BOARD_COLS);
        thisPlayerState = new State();
    }

    public Player getThisPlayer(){
        return thisPlayer;
    }

    public Player getOpponentShadow() {
        return opponentShadow;
    }

    public int getState(){
        return thisPlayerState.currentState;
    }

    public Role getRole(){
        return thisPlayerRole;
    }

    public void setState(int state) {
        thisPlayerState.currentState = state;
    }

    public void setServerRole(){
        thisPlayerRole = new BattleShipServer();
    }

    public void setClientRole(String serverName){
        thisPlayerRole = new BattleShipClient(serverName);
    }


    public void resetGameBoard(){
        thisPlayer.reinitializeBoard();
        opponentShadow.reinitializeBoard();
    }
}
