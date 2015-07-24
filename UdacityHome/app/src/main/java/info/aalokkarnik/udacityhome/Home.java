package info.aalokkarnik.udacityhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    public void onClick(View view)
    {
        // All App Button clicks are handled here
        // Section will be updated as projects develop
        // New apps/intents will be launched from here

        String message="";

        switch (view.getId()) {
            case R.id.btn_spotifyStreamer: {
                message = "Spotify Streamer App!";
                break;
            }
            case R.id.btn_scoresApp: {
                message = "Scores App!";
                break;
            }
            case R.id.btn_libraryApp: {
                message = "Library App!";
                break;
            }
            case R.id.btn_buildItBigger: {
                message = "Build It Bigger App!";
                break;
            }
            case R.id.btn_xyzReader: {
                message = "XYZ Reader App!";
                break;
            }
            case R.id.btn_capstoneApp: {
                message = "Capstone App!";
                break;
            }
        }

        Toast.makeText(this, "This button will launch the " + message, Toast.LENGTH_SHORT).show();
    }
}
