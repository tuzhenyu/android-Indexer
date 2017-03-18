package indexer.tzy.com.indexer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import indexer.tzy.com.indexer.index.SideBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bn1;

    Button bn2;

    ListView listView;

    SideBar sideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bn1 = (Button) findViewById(R.id.bn1);
        bn2 = (Button) findViewById(R.id.bn2);
        sideBar = (SideBar) findViewById(R.id.sideBar);
        listView = (ListView) findViewById(R.id.list_view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bn1:
                break;
            case R.id.bn2:
                break;
            default:
        }
    }


}
