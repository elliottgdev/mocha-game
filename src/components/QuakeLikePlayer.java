package components;

import entities.Entity;
import experimental.levels.Level;
import experimental.levels.Sector;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.security.Key;

import static com.sun.javafx.util.Utils.clamp;
import static experimental.Maths.lineCircleCollision;
import static experimental.levels.Level.getSectors;
import static java.lang.Math.*;
import static org.lwjgl.util.vector.Vector2f.dot;
import static renderEngine.DisplayManager.getDeltaTime;

public class QuakeLikePlayer implements Component{
    private Vector2f forwardVector = new Vector2f(), sideVector = new Vector2f();
    private Vector2f wishDir = new Vector2f();
    private Vector2f velocity = new Vector2f();
    private final float speed = 12f;
    private final float friction = 1f;
    private final float gravity = 0.02f;
    private final float jumpForce = 0.36f;
    private final float playerRadius = 0.5f;
    private float yVelocity = 0;
    public final float height = 2.5f;
    private float sensitivity = 0.5f;
    private boolean grounded = true;
    private Entity player;

    Vector2f vel = new Vector2f();
    public float pitch = 0;
    private final float lookClamp = 90;

    private Sector[] sectors = getSectors();

    @Override
    public void awake(Entity entity) {
         player = entity;
    }

    @Override
    public void update(Entity entity) {
        movementInput();
        getWishDir();

        if (grounded){
            vel = groundAcceleration();
        }
        else {
            vel = airAcceleration();
        }

        entity.getPosition().x += (vel.x);
        entity.getPosition().z += (vel.y);

        entity.setRotY(entity.getRotY() - Mouse.getDX() * sensitivity);
        pitch -= Mouse.getDY() * sensitivity;

        if (pitch >= lookClamp) pitch = 90;
        if (pitch <= -lookClamp) pitch = -90;
    }

    @Override
    public void tick(Entity entity) {
        gravity();
    }

    private void gravity(){
        Vector2f sectorHeights = Level.getFloorHeight(new Vector2f(player.getPosition().x, player.getPosition().z));

        for (Sector sector : sectors){
            if (sector.floorHeight > sectorHeights.x) {
                if (lineCircleCollision(sector.v0, sector.v1, new Vector2f(player.getPosition().x, player.getPosition().z), playerRadius)) {
                    sectorHeights.x = sector.floorHeight;
                    sectorHeights.y = sector.ceilingHeight;
                } else if (lineCircleCollision(sector.v1, sector.v3, new Vector2f(player.getPosition().x, player.getPosition().z), playerRadius)) {
                    sectorHeights.x = sector.floorHeight;
                    sectorHeights.y = sector.ceilingHeight;
                } else if (lineCircleCollision(sector.v3, sector.v2, new Vector2f(player.getPosition().x, player.getPosition().z), playerRadius)) {
                    sectorHeights.x = sector.floorHeight;
                    sectorHeights.y = sector.ceilingHeight;
                } else if (lineCircleCollision(sector.v0, sector.v2, new Vector2f(player.getPosition().x, player.getPosition().z), playerRadius)) {
                    sectorHeights.x = sector.floorHeight;
                    sectorHeights.y = sector.ceilingHeight;
                }
            }
        }

        yVelocity -= gravity;
        player.getPosition().y += yVelocity;
        if (player.getPosition().y > sectorHeights.x){
            grounded = false;
        } else {
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
            inputFactorY -= 1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            inputFactorY += 1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            inputFactorX -= 1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            inputFactorX += 1f;
        }

        if (grounded && Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            yVelocity = jumpForce;
            grounded = false;
            player.getComponent(AudioSource.class).play("res/bounce.wav");
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
        float addSpeed = clamp((speed - currentSpeed) * getDeltaTime(), 0, getDeltaTime());
        return new Vector2f(velocity.x + addSpeed * wishDir.x, velocity.y + addSpeed * wishDir.y);
    }

    private Vector2f airAcceleration(){
        float currentSpeed = dot(velocity, wishDir);
        float addSpeed = clamp((speed - currentSpeed) * getDeltaTime(), 0, getDeltaTime());
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
