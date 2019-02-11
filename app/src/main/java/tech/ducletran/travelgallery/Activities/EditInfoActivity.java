package tech.ducletran.travelgallery.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.*;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import tech.ducletran.travelgallery.Fragment.MapFragment;
import tech.ducletran.travelgallery.Fragment.PhotosFragment;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EditInfoActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final float DEFAULT_ZOOM = 15f;

    private String currentDescription;
    private String currentTitle;
    private ImageData currentImage;
    private Date currentDate;

    private EditText dateEditText;
    private EditText descriptionEditText;
    private EditText titleEditText;
    private AutoCompleteTextView locationSearchTextView;
    private ImageButton chooseNewLocationButton;
    private ImageButton returnFirstLocationButton;
    private GoogleMap map;
    private MarkerOptions options;

    private boolean locationChanged = false;

    // Google Places is commented because for now I haven't managed to get the API key
//    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG,Place.Field.NAME);
//    private static final int AUTOCOMPLETE_REQUEST_CODE = 1919;
//    private Place placeFound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        // Get widget
        ImageButton dateImageButton = findViewById(R.id.edit_info_date_image_button);
        dateEditText = findViewById(R.id.edit_info_date_edit_text);
        titleEditText = findViewById(R.id.edit_info_title_edit_text);
        descriptionEditText = findViewById(R.id.edit_info_description_edit_text);
        locationSearchTextView = findViewById(R.id.edit_info_search_text_view);
        chooseNewLocationButton = findViewById(R.id.edit_info_choose_new_location_button);
        returnFirstLocationButton = findViewById(R.id.edit_info_return_home_location_button);

        // Get current image information
        Intent intent = getIntent();
        int imageId = intent.getIntExtra("edit_info_id",0);
        currentImage = ImageManager.getImageById(imageId);
        final String latitude = currentImage.getLatitude();
        final String longtitude = currentImage.getLongtitude();
        currentTitle = currentImage.getTitle();
        currentDescription = currentImage.getDescription();
        String date = currentImage.getDateFormatted();

        // Set up title and description
        String currentDateFormatted = "";
        titleEditText.setText(currentTitle);
        descriptionEditText.setText(currentDescription);

        // Set up date
        try {
            currentDate = new SimpleDateFormat("dd MMMM yyyy").parse(date);
            currentDateFormatted = new SimpleDateFormat("dd/MM/yyyy").format(currentDate);
            dateEditText.setText(currentDateFormatted);

        } catch (ParseException e) {
            Toast.makeText(this,
                    "An error has occured with date. Please report to developer",Toast.LENGTH_LONG).show();
        }

        dateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // Do something to change date
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(currentDate);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditInfoActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Date newDate = new GregorianCalendar(year,month,dayOfMonth).getTime();
                                dateEditText.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate));
                                currentImage.setNewDate(newDate);
                                DisplayImageInfoActivity.setInfoChanged();
                                PhotosFragment.setPhotoFragmentChanged();
                            }
                        },
                        year, month, day
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


//        // Set up location
//        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_map_api_key));
//
//        // Create a new Places client instance.
//        PlacesClient placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.edit_info_map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (latitude != null && longtitude != null) {
                    LatLng currentLocation = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longtitude));
                    moveCamera(currentLocation);
                    setLocation(currentLocation,titleEditText.getText().toString());
                }
            }
        });

        locationSearchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchLocation();
                    return true;
                }

//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields)
//                        .build(EditInfoActivity.this);
//                startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE);
                return false;
            }
        });
//        locationSearchTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields)
//                        .build(EditInfoActivity.this);
//                startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE);
//            }
//        });

        chooseNewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition cameraLocation = map.getCameraPosition();
                setLocation(new LatLng(cameraLocation.target.latitude,cameraLocation.target.longitude),
                        titleEditText.getText().toString());
//                if (placeFound.getName() != null && titleEditText.getText() == null) {
//                    titleEditText.setText(placeFound.getName());
//                }
                locationChanged = true;
            }
        });

        returnFirstLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longtitude)) {
                    moveCamera(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longtitude)));
                } else {
                    Toast.makeText(EditInfoActivity.this,"Photo don't have default location",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_info_activity_album,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_saving:
                String newTitle = titleEditText.getText().toString();
                String newDescription = descriptionEditText.getText().toString();
                if (!newTitle.equals(currentTitle)) {
                    currentImage.setNewTitle(newTitle);
                    DisplayImageInfoActivity.setInfoChanged();
                }
                if (!newDescription.equals(currentDescription)) {
                    currentImage.setNewDescription(newDescription);
                    DisplayImageInfoActivity.setInfoChanged();
                }

                if (locationChanged) {
                    Double newLatitude = options.getPosition().latitude;
                    Double newLongtitude = options.getPosition().longitude;
                    currentImage.setNewLocation(Double.toString(newLatitude),Double.toString(newLongtitude));
                    DisplayImageInfoActivity.setInfoChanged();
                    MapFragment.addNewImageMarker(currentImage.getImageMarker());
                }

                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void moveCamera(LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));

    }

    private void setLocation(LatLng latLng, String title) {
        options = new MarkerOptions().position(latLng).title(title);

        if (!TextUtils.isEmpty(title)) {
            options.title(title);
        }
        map.clear();
        map.addMarker(options);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE
//                && resultCode == RESULT_OK) {
//            placeFound = Autocomplete.getPlaceFromIntent(data);
//            moveCamera(placeFound.getLatLng());
//        }
//    }

    private void searchLocation() {
        String searchString = locationSearchTextView.getText().toString();

        Geocoder geocoder = new Geocoder(EditInfoActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString,1);
        } catch (IOException e) { }

        if (list.size() > 0) {
            Address address = list.get(0);
            turnDownKeyBoard();
            // Can toast this location
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()));
        }
    }

    private void turnDownKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
