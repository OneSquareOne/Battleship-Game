public class Model {
    Controller gameController;
    
    private Player thisPlayer;
	private Player opponentShadow; 
	private State thisPlayerState;

    public Model(Controller controller){
        gameController = controller;
        gameController.registerModel(this);
    }
}
