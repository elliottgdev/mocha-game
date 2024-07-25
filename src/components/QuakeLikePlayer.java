package components;

import entities.Entity;
import experimental.levels.Level;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.security.Key;

import static com.sun.javafx.util.Utils.clamp;
import static java.lang.Math.*;
import static org.lwjgl.util.vector.Vector2f.dot;
import static renderEngine.DisplayManager.getDeltaTime;

public class QuakeLikePlayer implements Component{
    private Vector2f forwardVector = new Vector2f(), sideVector = new Vector2f();
    private Vector2f wishDir = new Vector2f();
    private Vector2f velocity = new Vector2f();
    private float speed = 25f;
    private float accel = 25f * speed;
    private float friction = 1f;
    private float gravity = 1.5f;
    private float yVelocity = 0;
    public final float height = 2.5f;
    private boolean grounded = true;
    private Entity player;

    Vector2f vel = new Vector2f();

    @Override
    public void awake(Entity entity) {
         player = entity;
    }

    @Override
    public void update(Entity entity) {
        gravity();
        movementInput();
        getWishDir();

        if (grounded){
            vel = groundAcceleration();
        }

        entity.setPosition(new Vector3f(entity.getPosition().x + (vel.x), entity.getPosition().y, entity.getPosition().z + (vel.y)));

        float turnSpeed = 200;
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) entity.setRotY(entity.getRotY() + turnSpeed * getDeltaTime());
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) entity.setRotY(entity.getRotY() - turnSpeed * getDeltaTime());
    }

    private void gravity(){
        player.getPosition().y += yVelocity;
        Vector2f sectorHeights = Level.getFloorHeight(new Vector2f(player.getPosition().x, player.getPosition().z));
        if (player.getPosition().y > sectorHeights.x){
            yVelocity -= gravity * getDeltaTime();
            grounded = false;
        } else if (player.getPosition().y <= sectorHeights.x){
            yVelocity = 0;
            player.getPosition().y = sectorHeights.x;
            grounded = true;
        }
    }

    private void movementInput(){
        forwardVector = new Vector2f(0, 0);
        sideVector = new Vector2f(0, 0);
        float inputFactorX = 0;
        float inputFactorY = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            inputFactorY -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            inputFactorY += 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            inputFactorX -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            inputFactorX += 1;
        }
        forwardVector = new Vector2f((float) (inputFactorY * sin(toRadians(player.getRotY()))), (float) (inputFactorY * cos(toRadians(player.getRotY()))));
        sideVector = new Vector2f((float) (inputFactorX * sin(toRadians(player.getRotY() + 90))), (float) (inputFactorX * cos(toRadians(player.getRotY() + 90))));
    }

    private void getWishDir(){
        if (abs(forwardVector.x + sideVector.x) > 0 || abs(forwardVector.y + sideVector.y) > 0)
            new Vector2f(forwardVector.x + sideVector.x, forwardVector.y + sideVector.y).normalise(wishDir);
        else
            wishDir = new Vector2f(forwardVector.x + sideVector.x, forwardVector.y + sideVector.y);
    }

    private Vector2f groundAcceleration(){
        velocity = friction(velocity);

        float currentSpeed = dot(velocity, wishDir);
        float addSpeed = clamp((speed - currentSpeed) * getDeltaTime(), 0, accel * getDeltaTime());
        return new Vector2f(velocity.x + addSpeed * wishDir.x, velocity.y + addSpeed * wishDir.y);
    }

    private Vector2f friction(Vector2f vel){
        float x = vel.x, y = vel.y;

        if (x > 0.04f)
            x -= friction * getDeltaTime();
        else if (x < -0.04f)
            x += friction * getDeltaTime();
        else
            x = 0;

        if (y > 0.04f)
            y -= friction * getDeltaTime();
        else if (x < -0.04f)
            y += friction * getDeltaTime();
        else
            y = 0;

        return new Vector2f(x, y);
    }
}
