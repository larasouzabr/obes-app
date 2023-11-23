package com.example.obes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.dao.AddressDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.User.User;
import com.example.obes.perfil.PerfilUserCommon;
import com.example.obes.perfil.PerfilUserInstitutional;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationPage extends AppCompatActivity implements OnMapReadyCallback {
    private EditText etCep;
    private EditText etState;
    private EditText etCity;
    private EditText etStreet;
    private EditText etNeighborhood;
    private EditText etNumber;
    private TextView tvTitlePage;
    private ImageView ivBackArrow;
    private Button buttonCancel;
    private Button buttonSave;
    private Button buttonSearch;
    private GoogleMap mMap;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);
        this.startComponents();

        this.tvTitlePage.setText("Localização");

        this.userLogged = this.getUserLogged();

        this.ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedFieldsEmpty()) {
                    Toast.makeText(LocationPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    showModalSaveAddress();
                }
            }
        });

        this.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedFieldsEmpty()) {
                    Toast.makeText(LocationPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    com.example.obes.model.Address.Address newAddress = getAddressForm();
                    addMarkerWithAddress(newAddress);
                }

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        AddressDAO addressDAO = AddressDAO.getInstance();
        com.example.obes.model.Address.Address userLoggedAddress = addressDAO.getAddressByIdUser(this.userLogged.getId());

        addMarkerWithAddress(userLoggedAddress);

        setInfAddress(userLoggedAddress);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                Geocoder geocoder = new Geocoder(LocationPage.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    // String fullAddress = "";
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);

                        String cep = address.getPostalCode();
                        String estado = address.getAdminArea();
                        String cidade = address.getSubAdminArea();
                        String rua = address.getThoroughfare();
                        String bairro = address.getSubLocality();
                        String numero = address.getSubThoroughfare();

                        // fullAddress = String.format("CEP: %s\nEstado: %s\nCidade: %s\nBairro: %s\nRua: %s\nNúmero: %s", cep, estado, cidade, bairro, numero);

                        com.example.obes.model.Address.Address addressMarker = new com.example.obes.model.Address.Address(0, 0, cep, estado, cidade, rua, bairro, (numero == null ? "" : numero));
                        setInfAddress(addressMarker);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Local Selecionado");
                mMap.clear();
                mMap.addMarker(markerOptions);
            }
        });
    }

    private void startComponents() {
        this.etCep = findViewById(R.id.cep);
        this.etState = findViewById(R.id.state);
        this.etCity = findViewById(R.id.city);
        this.etStreet = findViewById(R.id.street);
        this.etNeighborhood = findViewById(R.id.neighborhood);
        this.etNumber = findViewById(R.id.number);
        this.tvTitlePage = findViewById(R.id.title_page);
        this.ivBackArrow = findViewById(R.id.back_arrow);
        this.buttonCancel = findViewById(R.id.button_cancel);
        this.buttonSave = findViewById(R.id.button_save);
        this.buttonSearch = findViewById(R.id.button_search);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }

    private User getUserLogged() {
        User user = this.loginSessionManager.getCurrentUserCommon();

        if (user == null) {
            user = this.loginSessionManager.getCurrentUserInstitutional();
        }

        return user;
    }

    private int countIdAddress() {
        int idAddress = 1;

        com.example.obes.model.Address.Address addressUser = AddressDAO.getInstance().getAddressByIdUser(this.userLogged.getId());

        if (addressUser != null) {
            return addressUser.getIdAddress();
        }

        int amountAddress = AddressDAO.getInstance().getListAddress().size();

        if (amountAddress > 0) {
            idAddress = AddressDAO.getInstance().getListAddress().get(amountAddress - 1).getIdAddress() + 1;
        }

        return idAddress;
    }

    private boolean checkedFieldsEmpty() {
        boolean someEmpty = false;
        String cep = this.etCep.getText().toString();
        String state = this.etState.getText().toString();
        String city = this.etCity.getText().toString();
        String neighborhood = this.etNeighborhood.getText().toString();
        String number = this.etNumber.getText().toString();

        if (cep.isEmpty() || state.isEmpty() || city.isEmpty() || neighborhood.isEmpty() || number.isEmpty()) {
            someEmpty = true;
        }

        return someEmpty;
    }

    private void setInfAddress(com.example.obes.model.Address.Address address) {
        if (address != null) {
            this.etCep.setText(address.getCep());
            this.etState.setText(address.getEstado());
            this.etCity.setText(address.getCidade());
            this.etStreet.setText(address.getRua());
            this.etNeighborhood.setText(address.getBairro());
            this.etNumber.setText(String.valueOf(address.getNumero()));
        }
    }

    private void addMarkerWithAddress(com.example.obes.model.Address.Address userLoggedAddress) {
        if (userLoggedAddress != null) {
            String fullAddress = userLoggedAddress.getNumero() + "\n" + userLoggedAddress.getBairro() + "\n" + userLoggedAddress.getCidade() + "\nCEP: " + userLoggedAddress.getCep() + "\n" + userLoggedAddress.getEstado();
            LatLng location = findLatLngByAddress(fullAddress);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title("Local Selecionado")
                    .snippet(fullAddress);

            mMap.clear();
            mMap.addMarker(markerOptions);

            float zoom = 15.0f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, zoom);

            mMap.moveCamera(cameraUpdate);
        }
    }

    private LatLng findLatLngByAddress(String fullAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocationName(fullAddress, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showModalSaveAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationPage.this);
        LayoutInflater inflater = (LayoutInflater) LocationPage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.modal_delete_book, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        TextView tvDescription = dialogView.findViewById(R.id.description);
        Button buttonConfirm = dialogView.findViewById(R.id.button_delete);
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);

        tvDescription.setText("Tem certeza de que deseja salvar o endereço?");
        buttonConfirm.setText("Salvar");

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressDAO addressDAO = AddressDAO.getInstance();

                com.example.obes.model.Address.Address newAddress = getAddressForm();

                if (addressDAO.getAddressByIdUser(userLogged.getId()) == null) {
                    System.out.println("não tem endereço " + addressDAO.getAddressByIdUser(userLogged.getId()));
                    addressDAO.addAddress(newAddress);
                } else {
                    System.out.println("Tem endereço " + addressDAO.getAddressByIdUser(userLogged.getId()));
                    addressDAO.editAddress(newAddress);
                }

                Intent intent;

                if (loginSessionManager.getCurrentUserCommon() != null) {
                    intent = new Intent(LocationPage.this, PerfilUserCommon.class);
                } else {
                    intent = new Intent(LocationPage.this, PerfilUserInstitutional.class);
                }

                startActivity(intent);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private com.example.obes.model.Address.Address getAddressForm() {
        String cep = etCep.getText().toString();
        String state = etState.getText().toString();
        String city = etCity.getText().toString();
        String street = etStreet.getText().toString();
        String neighborhood = etNeighborhood.getText().toString();
        String number = etNumber.getText().toString();

        com.example.obes.model.Address.Address newAddress = new com.example.obes.model.Address.Address(countIdAddress(), userLogged.getId(), cep, state, city, street, neighborhood, number);

        return newAddress;
    }
}