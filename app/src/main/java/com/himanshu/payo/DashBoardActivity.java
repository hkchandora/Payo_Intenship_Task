package com.himanshu.payo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.himanshu.payo.adapter.DataAdapter;
import com.himanshu.payo.model.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    private static final String URL = "https://reqres.in/api/users?page=";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private CardView profileBtn;
    private List<Data> listItem;
    private ProgressBar progressBar;
    private int page = 1;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        profileBtn = findViewById(R.id.profile);

        progressBar = findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItem = new ArrayList<>();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        fetchData(page);

        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    fetchData(page);
                }
            }
        });
    }

    private void fetchData(final int page) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL + page + "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");

                            if (array.length() < 1) {
                                Toast.makeText(DashBoardActivity.this, "No Data in Page "+page, Toast.LENGTH_SHORT).show();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                Data model = new Data(
                                        object.getString("email"),
                                        object.getString("first_name"),
                                        object.getString("last_name"),
                                        object.getString("avatar")
                                );
                                listItem.add(model);
                            }

                            adapter = new DataAdapter(listItem, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DashBoardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(DashBoardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void profileIcon(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, profileBtn, "profile");
        startActivity(intent, options.toBundle());
    }

    public void firstNameSorting(View view) {
        Collections.sort(listItem, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return o1.getFirst_name().compareTo(o2.getFirst_name());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void lastNameSorting(View view) {
        Collections.sort(listItem, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return o1.getLast_name().compareTo(o2.getLast_name());
            }
        });
        adapter.notifyDataSetChanged();
    }
}