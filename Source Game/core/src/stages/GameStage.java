package stages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.GameScreen;

import actors.Background;
import actors.Enemy;
import actors.Enemy2;
import actors.Enemy3;
import actors.Ground;
import actors.RestartButton;
import actors.Runner;
import thread.ardNetwork;
import utils.BodyUtils;
import utils.Constants;
import utils.WorldUtils;

public class GameStage extends Stage implements ContactListener {
	// This will be our viewport measurements while working with the debug renderer
	private Label outputLabel;
	//
	public BufferedReader reader;
	public int data = 0;
	//
    private World world;
    private Ground ground;
    private Runner runner;
    private Button button2;
    private Button button3;
    private int temp = 0;
    private final float TIME_STEP = 1/300f;
    private float accumulator = 6f;
    private OrthographicCamera camera;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;
    
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private Vector3 touchPoint;
    public int datalog = 1;
    public int inputdata = 0;
    public ardNetwork ardNetwork;
    
    public GameStage() {
    	super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
        setUpWorld();
        setupCamera();
        setupTouchControlAreas();
        //getdata
    	setupNetwork(this.runner);
    }

    

    private void setupNetwork(Runner runner) {
		// TODO Auto-generated method stub
		ardNetwork = new ardNetwork(runner);
		ardNetwork.start();
	}



	private void setUpWorld() {
        world = WorldUtils.createWorld();
        // Let the world now you are handling contacts
        world.setContactListener(this);
        setUpBackground();
        setUpGround();
        
        createEnemy();
        setUpRunner();
        GameScreen.resetScore();
        //GameScreen.resetGO();

    }
    private void setUpButton() {
    	int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;
    	Skin mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
    	button2 = new TextButton("NEW GAME",mySkin,"small");
    	button2.setSize(col_width*4,row_height);
    	button2.setPosition(col_width*4,Gdx.graphics.getHeight()-row_height*3);
    	button2.addListener(new InputListener(){
    	    @Override
    	    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

    	    }
    	    @Override
    	    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
    	        setUpWorld();
    	        button2.addAction(Actions.removeActor());
    	        GameScreen.resetGO();
    	        return true;
    	    }
    	});
    	//game over 
    	if (temp == 0) {
			GameScreen.resetGO();
		}else GameScreen.getGO();
    	
    	addActor(button2);
    	temp = 1;
    }

	private void setUpBackground() {
		// TODO Auto-generated method stub
		
		addActor(new Background());	
	}



	private void setUpGround() {
        ground = new Ground(WorldUtils.createGround(world));
        addActor(ground);
    }

    private void setUpRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
    }
    
    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }
    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        screenLeftSide = new Rectangle(0, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2,
                getCamera().viewportHeight);
        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }
       // System.out.println(ardNetwork.getDataAction());
        performevent(ardNetwork.getDataAction());
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    private void performevent(int data) {
		// TODO Auto-generated method stub
		switch(data) {
		case 0:
			runner.dodge();
			break;
		case 1:
			if (runner.isDodging()) {
				runner.stopDodge();
			}
			break;
		case 2:
			if (runner.isDodging()) {
				runner.stopDodge();
			}
			runner.jump();
			break;
		default:
		}
	}
    private void update(Body body) {
    	//GameScreen.resetGO();
        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && !runner.isHit()) {
               if(GameScreen.getScore() > 8) {
            	   createEnemy3();
               }else  if(GameScreen.getScore() > 3) {
		            	  createEnemy2();
		               }else {
		            	   createEnemy();
		               }
               GameScreen.incScore();
               
               GameScreen.incHighScore();
               
            }
            world.destroyBody(body);
            
        }
    }
    
    private void createEnemy() {
        Enemy enemy = new Enemy(WorldUtils.createEnemy(world));
        addActor(enemy);
    }
    private void createEnemy2() {
        Enemy2 enemy = new Enemy2(WorldUtils.createEnemy(world));
        addActor(enemy);
    }
    private void createEnemy3() {
        Enemy3 enemy = new Enemy3(WorldUtils.createEnemy(world));
        addActor(enemy);
    }
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        // Need to get the actual coordinates
        translateScreenToWorldCoordinates(x, y);

        if (rightSideTouched(touchPoint.x, touchPoint.y)) {
            runner.jump();
        }else if (leftSideTouched(touchPoint.x, touchPoint.y)) {
            runner.dodge();
        }

        return super.touchDown(x, y, pointer, button);
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (runner.isDodging()) {
            runner.stopDodge();
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }
    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }
    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }

    /**
     * Helper function to get the actual coordinates in my world
     * @param x
     * @param y
     */
    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }
    @Override
    public void beginContact(Contact contact) {

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) {
            runner.hit();
            setUpButton();
        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            runner.landed();
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    
}
