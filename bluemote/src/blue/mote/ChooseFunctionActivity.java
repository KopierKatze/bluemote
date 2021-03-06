package blue.mote;

import java.util.UUID;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseFunctionActivity extends ListActivity {

	UUID uuid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uuid = UUID.fromString(getString(R.string.bluemote_uuid));
		
		final String[] functions = new String[] {
				getString(R.string.presentation_function),
				getString(R.string.vlc_function) };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, functions);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Class<?> cls = null;
				if (position == 0)
					cls = PresentationFunctionActivity.class;
				else if (position == 1)
					cls = VlcFunctionActivity.class;

				if (cls == null)
					showMessage("unknown function "
							+ ((TextView) view).getText());
				else {
					Intent intent = new Intent(ChooseFunctionActivity.this, cls);
					startActivity(intent);
				}
			}
		});
	}

	

	void showMessage(CharSequence s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
}
