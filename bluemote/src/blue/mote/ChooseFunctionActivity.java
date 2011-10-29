package blue.mote;

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

	static final String[] FUNCTIONS = new String[] { "Presentation", "VLC",
			"Suicid" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, FUNCTIONS);
		setListAdapter(adapter);

		final ListActivity that = this;

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

				CharSequence toast_msg = ((TextView) view).getText();
				if (cls == null)
					toast_msg = "unknown function " + toast_msg;

				Toast.makeText(getApplicationContext(), toast_msg,
						Toast.LENGTH_SHORT).show();

				if (cls != null) {
					Intent intent = new Intent(that, cls);
					startActivity(intent);
				}
			}
		});
	}
}
