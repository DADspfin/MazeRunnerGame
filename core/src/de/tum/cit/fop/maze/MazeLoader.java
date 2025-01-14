package de.tum.cit.fop.maze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MazeLoader extends  ApplicationAdapter{

    private static final float PPM = 23.5f;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera = new OrthographicCamera();
    FitViewport viewport;
    private GameScreen gameScreen;

    public MazeLoader(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public TiledMap create(String filePath){
        TmxMapLoader loader = new TmxMapLoader();
        tiledMap = loader.load(filePath);
        //parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
        parseTileCollisions();
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 640);
        viewport = new FitViewport(640, 640, camera);
        return tiledMap;
    }
    public void render(){
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.setView((OrthographicCamera) viewport.getCamera());
        renderer.render();
    }

    /*private void parseMapObjects(MapObjects mapObjects){
        for(MapObject mapObject : mapObjects){
            if(mapObject instanceof PolygonMapObject){
                createStaticBody((PolygonMapObject) mapObject);
            }
        }
    }*/

    private void parseTileCollisions() {
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer instanceof TiledMapTileLayer) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

                for (int x = 0; x < tileLayer.getWidth(); x++) {
                    for (int y = 0; y < tileLayer.getHeight(); y++) {
                        TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                        if (cell == null || cell.getTile() == null) continue;

                        TiledMapTile tile = cell.getTile();

                        // Check if the tile has collision objects
                        if (tile.getObjects().getCount() > 0) {
                            BodyDef bodyDef = new BodyDef();
                            bodyDef.type = BodyDef.BodyType.StaticBody;
                            bodyDef.position.set((x + 0.5f) / PPM, (y + 0.5f) / PPM);
                            Body body = gameScreen.getWorld().createBody(bodyDef);

                            for (MapObject object : tile.getObjects()) {
                                if (object instanceof PolygonMapObject) {
                                    PolygonShape shape = (PolygonShape) createPolygonShape((PolygonMapObject) object);
                                    body.createFixture(shape, 1.0f);
                                    shape.dispose();
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void createStaticBody(PolygonMapObject polygonMapObject){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1.0f);
        shape.dispose();

    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject){
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length/2];
        for(int i = 0; i < vertices.length/2; i++){
            Vector2 current = new Vector2(vertices[i*2] / PPM, vertices[i*2+1]/PPM);
            worldVertices[i] = current;
        }
        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;

    }
}

