package tech.ducletran.travelgallery.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;
import tech.ducletran.travelgallery.R;

public class StoryPageFragment extends Fragment {

    public StoryPageFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        Bundle bundle = getArguments();
        String storyDetails = bundle.getString("page_details");
        try {
            JSONObject jsonObject = new JSONObject(storyDetails);
            String image = jsonObject.getString("page_image");
            String normalText = jsonObject.getString("page_normal_text");
            String specialText = jsonObject.getString("page_special_text");
            int pageType = jsonObject.getInt("page_type");
            if (pageType == 1) {

                view = inflater.inflate(R.layout.story_page_type_1_view,container,false);
                ((TextView) view.findViewById(R.id.story_page_type_1_text_view)).setText(specialText);
                if (!TextUtils.isEmpty(image)) {
                    Glide.with(getContext()).load(image)
                            .into((ImageView) view.findViewById(R.id.story_page_type_1_image_view));
                }
            } else if (pageType == 2) {
                view = inflater.inflate(R.layout.story_page_type_2_view,container,false);

                ((TextView) view.findViewById(R.id.story_page_type_2_title_text_view)).setText(specialText);
                ((TextView) view.findViewById(R.id.story_page_type_2_description_text_view)).setText(normalText);
                ImageView imageView = view.findViewById(R.id.story_page_type_2_image_view);

                if (TextUtils.isEmpty(image)) {
                    imageView.setVisibility(View.GONE);
                } else {
                    Glide.with(getContext()).load(image).into(imageView);
                }

            } else if (pageType == 3) {
                view = inflater.inflate(R.layout.story_page_type_3_view,container,false);

                Glide.with(getContext()).load(image).into((ImageView) view.findViewById(R.id.story_page_type_3_image_view));
                ((TextView) view.findViewById(R.id.story_page_type_3_text_view)).setText(normalText);
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(),"JSON Exception. Please report.",Toast.LENGTH_SHORT).show();
        }


        return view;
    }
}
