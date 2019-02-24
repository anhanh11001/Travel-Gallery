package tech.ducletran.travelgallery.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import tech.ducletran.travelgallery.Adapter.CityCountryItemAdapter;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;
import java.util.Comparator;

public class CountryFragment extends Fragment {

    private static ListView listView;
    private static ArrayList<String> countriesList;
    private static CityCountryItemAdapter adapter;
    private static TextView textView;


    public CountryFragment() {super();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_country_view,container,false);

        listView = view.findViewById(R.id.city_country_list_view);
        textView = view.findViewById(R.id.city_country_text_view);
        TextView titleTextView = view.findViewById(R.id.city_country_title_text_view);

        countriesList = new ArrayList<>(MapFragment.getCountriesList());
        countriesList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        textView.setText("You have travelled to " + countriesList.size() + " countries.");
        titleTextView.setText("List of countries");

        adapter = new CityCountryItemAdapter(getActivity(), countriesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
                editDialog.setTitle("Editting this country");
                editDialog.setMessage("Change the name of this country or delete it");

                final String currentName = countriesList.get(position);
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rename_album_layout,null);
                final EditText dialogEditText = dialogView.findViewById(R.id.album_rename_edit_text);

                editDialog.setView(dialogView);
                dialogEditText.setText(currentName);

                editDialog.setNegativeButton("delete",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        countriesList.remove(position);
                        adapter.notifyDataSetChanged();
                        listView.invalidate();
                        MapFragment.deleteCountry(currentName);
                        dialog.cancel();
                        textView.setText("You have travelled to " + countriesList.size() + " countries.");
                    }
                });

                editDialog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                editDialog.setPositiveButton("edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = dialogEditText.getText().toString();
                        if (!TextUtils.isEmpty(newName) && newName.equals(currentName)) {
                            countriesList.remove(position);
                            countriesList.add(position,currentName);
                            MapFragment.editCountry(currentName,newName);
                            adapter.notifyDataSetChanged();
                            listView.invalidate();
                        } else {
                            Toast.makeText(getActivity(),"Name of this city is not good enough",Toast.LENGTH_SHORT).show();
                        }

                        dialog.cancel();
                    }
                });

                editDialog.show();
            }
        });

        return view;
    }

    public static void addCountry(String name) {
        countriesList.add(name);
        textView.setText("You have travelled to " + countriesList.size() + " countries.");
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }
}
