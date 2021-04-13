package com.example.cycletrack10;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;


import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnSeekBarChangeListener, OnItemSelectedListener {

    private GoogleMap mMap;


    // City locations for mutable polyline.
    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
    private static final LatLng DARWIN = new LatLng(-12.4258647, 130.7932231);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    private static final LatLng PERTH = new LatLng(-31.95285, 115.85734);

    // Airport locations for geodesic polyline.
    private static final LatLng AKL = new LatLng(-37.006254, 174.783018);
    private static final LatLng JFK = new LatLng(40.641051, -73.777485);
    private static final LatLng LAX = new LatLng(33.936524, -118.377686);
    private static final LatLng LHR = new LatLng(51.471547, -0.460052);

    private static final int MAX_WIDTH_PX = 100;
    private static final int MAX_HUE_DEGREES = 360;
    private static final int MAX_ALPHA = 255;
    private static final int CUSTOM_CAP_IMAGE_REF_WIDTH_PX = 50;
    private static final int INITIAL_STROKE_WIDTH_PX = 5;

    private static final int PATTERN_DASH_LENGTH_PX = 50;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final Dot DOT = new Dot();
    private static final Dash DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);
    private static final List<PatternItem> PATTERN_DASHED = Arrays.asList(DASH, GAP);
    private static final List<PatternItem> PATTERN_MIXED = Arrays.asList(DOT, GAP, DOT, DASH, GAP);

    private Polyline mutablePolyline;
    private SeekBar hueBar;
    private SeekBar alphaBar;
    private SeekBar widthBar;
    private Spinner startCapSpinner;
    private Spinner endCapSpinner;
    private Spinner jointTypeSpinner;
    private Spinner patternSpinner;
    private CheckBox clickabilityCheckbox;

    // These are the options for polyline caps, joints and patterns. We use their
    // string resource IDs as identifiers.

    private static final int[] CAP_TYPE_NAME_RESOURCE_IDS = {
            R.string.cap_butt, // Default
            R.string.cap_round,
            R.string.cap_square,
            R.string.cap_image,
    };

    private static final int[] JOINT_TYPE_NAME_RESOURCE_IDS = {
            R.string.joint_type_default, // Default
            R.string.joint_type_bevel,
            R.string.joint_type_round,
    };

    private static final int[] PATTERN_TYPE_NAME_RESOURCE_IDS = {
            R.string.pattern_solid, // Default
            R.string.pattern_dashed,
            R.string.pattern_dotted,
            R.string.pattern_mixed,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        hueBar = findViewById(R.id.hueSeekBar);
        hueBar.setMax(MAX_HUE_DEGREES);
        hueBar.setProgress(0);

        alphaBar = findViewById(R.id.alphaSeekBar);
        alphaBar.setMax(MAX_ALPHA);
        alphaBar.setProgress(MAX_ALPHA);

        widthBar = findViewById(R.id.widthSeekBar);
        widthBar.setMax(MAX_WIDTH_PX);
        widthBar.setProgress(MAX_WIDTH_PX / 2);

        startCapSpinner = findViewById(R.id.startCapSpinner);
        startCapSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                getResourceStrings(CAP_TYPE_NAME_RESOURCE_IDS)));

        endCapSpinner = findViewById(R.id.endCapSpinner);
        endCapSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                getResourceStrings(CAP_TYPE_NAME_RESOURCE_IDS)));

        jointTypeSpinner = findViewById(R.id.jointTypeSpinner);
        jointTypeSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                getResourceStrings(JOINT_TYPE_NAME_RESOURCE_IDS)));

        patternSpinner = findViewById(R.id.patternSpinner);
        patternSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                getResourceStrings(PATTERN_TYPE_NAME_RESOURCE_IDS)));

        clickabilityCheckbox = findViewById(R.id.toggleClickability);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
