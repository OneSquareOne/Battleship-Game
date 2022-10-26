public class Model {
    Controller gameController;
    	private Player thisPlayer; // TODO: Move to model
	private Player opponentShadow; // TODO:Move to model
	private State thisPlayerState;

    public Model(Controller controller){
        gameController = controller;
        gameController.registerModel(this);
    }
}
