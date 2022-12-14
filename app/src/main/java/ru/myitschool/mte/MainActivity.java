package ru.myitschool.mte;

import static ru.myitschool.mte.Utils.BASE_URL;
import static ru.myitschool.mte.Utils.BLANK_URL;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.myitschool.mte.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    TextView tvResult;
    EditText etSearch;
    Button btSearch;
    WebView wvIcon;
    Retrofit retrofit;


    /**
     * Uses API
     * https://www.mediawiki.org/wiki/API:REST_API
     * https://en.wikipedia.org/w/rest.php/v1/search/page?q=Samsung&limit=1
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvResult = binding.content.tvResult;
        etSearch = binding.content.etFind;
        btSearch = binding.content.btSearch;
        wvIcon = binding.content.wvIcon;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WikiService service = retrofit.create(WikiService.class);

        wvIcon.getSettings().setBuiltInZoomControls(true);



        btSearch.setOnClickListener(view -> {
            wvIcon.loadUrl(BLANK_URL);
            tvResult.setText("");
            Call<SearchRes> call = service.find(etSearch.getText().toString(), 1);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<SearchRes> call, @NonNull Response<SearchRes> response) {
                    SearchRes res = response.body();
                    if (response.isSuccessful() && res.pages != null && res.pages.length > 0) {
                        tvResult.setText(Html.fromHtml(res.pages[0].excerpt, Html.FROM_HTML_MODE_COMPACT));
                        if (res.pages[0].thumbnail != null)
                            wvIcon.loadUrl("https:" + res.pages[0].thumbnail.url);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SearchRes> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, getResources().getText(R.string.connection_problems), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}
