package com.marioJmartDR.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.marioJmartDR.AboutMeActivity;
import com.marioJmartDR.R;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.request.RequestFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    private EditText edtPage;
    private Button prevBtn, nextBtn, goBtn;
    private ListView listView;
    List<Product> productList = new ArrayList<>();
//    List<String> productNameList = new ArrayList<>();
    private static final Gson gson = new Gson();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        edtPage = v.findViewById(R.id.edt_page);
        prevBtn = v.findViewById(R.id.prev_btn);
        nextBtn = v.findViewById(R.id.next_btn);
        listView = v.findViewById(R.id.product_list);
        goBtn = v.findViewById(R.id.go_btn);
        String page = edtPage.getText().toString();
        int pageNumber = Integer.valueOf(page);
        getProductList(pageNumber - 1, 1);          //page di backend dimulai dari 0
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageNow = Integer.valueOf(edtPage.getText().toString());
                edtPage.setText((pageNow + 1) + "");
                int pageNext = Integer.valueOf(edtPage.getText().toString());
                getProductList(pageNext - 1, 1);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageNow = Integer.valueOf(edtPage.getText().toString());
                edtPage.setText((pageNow - 1) + "");
                int pagePrev = Integer.valueOf(edtPage.getText().toString());
                getProductList(pagePrev - 1, 1);
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageNow = Integer.valueOf(edtPage.getText().toString());
                getProductList(pageNow - 1, 1);
            }
        });
        return v;
    }

    public void getProductList(int page, int pageSize){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    JSONArray object = new JSONArray(response);
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, productListType);
                    List<String> productNameList = new ArrayList<>();
                    for(Product p : productList){
                        productNameList.add(p.name);
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.row_product_list, productNameList);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(RequestFactory.getPage("product", page, pageSize, listener, errorListener));
    }
}