package com.example.usercosima.project3;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

// external libaries
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {

    MapView mapView;
    ImageView panoramaView;
    MapController mapController;
    Marker redMarker;
    Float touchPosition;
    Panorama panorama1;
    Panorama panorama2;
    Panorama currentPanorama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LocationManager MyLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //Create Panoramas and add Buildings to them
        panorama1 = new Panorama("panorama1",3, R.drawable.panorama1, 52.28540537, 8.02108169);
        panorama2 = new Panorama("panorama2", 3, R.drawable.panorama2, 52.28523801, 8.02167177);
        panorama1.addBuilding(0,0,250,"Grünes Haus");
        panorama1.addBuilding(1, 300, 500, "IGF Haus");
        panorama1.addBuilding(2, 800, 1400, "Werkstatt Haus");
        panorama2.addBuilding(0, 50, 600,"Ufo");
        panorama2.addBuilding(1, 900, 1200, "Grünes Haus");
        panorama2.addBuilding(2, 1250, 1496, "Werkstatt Haus");
        currentPanorama = panorama1;

        //Set the MapView Attributs
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        //Define the RedMarker
        redMarker = new Marker(mapView);
        redMarker.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.circlered));
        redMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        mapView.getOverlays().add(redMarker);

        //Add the GeoPoint of each Panorama to the map
        addMarker(panorama1.point, panorama1);
        addMarker(panorama2.point, panorama2);

        // Set Zoom and animate to the first point
        mapController = (MapController) mapView.getController();
        mapController.setZoom(18);
        mapController.animateTo(currentPanorama.point);

        // Default Panorama settings (first Panorama is displayed)
        panoramaView = (ImageView)findViewById(R.id.panoramaView);
        panoramaView.setImageResource(currentPanorama.panoramaId);
        redMarker.setPosition(currentPanorama.point);
        mapView.invalidate();

        // The OnTouchListener fetches the X-Position from the ImageView, whenever the ImageView is touched
        panoramaView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchPosition = event.getX();
                }
                return false;
            }
        });

        // The ClickListeren checks, if the current touchPosition is confirm with the angle of one of the Buildings. If it is confirmed
        // a Toast is display on the device with the according Building informations
        panoramaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPanorama.CheckAngle(touchPosition);

                if(currentPanorama.ToastInfo != null && !currentPanorama.ToastInfo.isEmpty()){
                    Toast.makeText(getApplicationContext(),currentPanorama.ToastInfo, Toast.LENGTH_SHORT).show();
                    currentPanorama.ToastInfo = null;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This method adds a new Marker at a specific point into the map. At the same time a clickListener is initialized
    public void addMarker(final GeoPoint point, final Panorama panoramaToMarker ){

        //Define the marker
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.circlegreen));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        //Add the marker to te overlays of the map
        mapView.getOverlays().add(marker);

        //refreshed the map
        mapView.invalidate();

        // Set up a new clickListener for the marker
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                                //When the marker is clicked, the matching com.example.usercosima.project3.Panorama is displayed in the panoramaView
                                                panoramaView.setImageResource(panoramaToMarker.panoramaId);
                                                redMarker.setPosition(point);
                                                redMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                                                mapView.invalidate();
                                                currentPanorama = panoramaToMarker;
                                                return false;
                                            }
                                        }
        );
    }
}
