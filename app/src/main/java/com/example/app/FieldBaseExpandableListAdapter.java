package com.example.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FieldBaseExpandableListAdapter extends BaseExpandableListAdapter {

	private static final int[] EMPTY_STATE_SET = {};
	private static final int[] GROUP_EXPANDED_STATE_SET = {android.R.attr.state_expanded};
	private static final int[][] GROUP_STATE_SETS = {
			EMPTY_STATE_SET, // 0
			GROUP_EXPANDED_STATE_SET // 1
	};

	private static int GROUP_BACKGROUND = Color.rgb(255, 255, 224);

    public LayoutInflater inflater;
    public Activity activity;

    List<Field> fields;

    public FieldBaseExpandableListAdapter(Activity act, List<Field> fields) {
        this.activity = act;
        this.inflater = act.getLayoutInflater();
        this.fields = fields;
    }

    @Override
    public int getGroupCount() {
        return fields == null ? 0 : fields.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return fields.get(groupPosition).childrenCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return fields.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return fields.get(groupPosition).getChild(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

	void adjustGroupView(View rootView, boolean isExpanded, boolean hasChildren) {
		// step 1: adjust group indicator
		ImageView ind = (ImageView) rootView.findViewById(R.id.explist_indicator);
		if (ind != null) {
			if (hasChildren) {
				ind.setVisibility(View.VISIBLE);
				int stateSetIndex = ( isExpanded ? 1 : 0) ;
				Drawable drawable = ind.getDrawable();
				drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
			} else {
				ind.setVisibility(View.INVISIBLE);
			}
		}

		// step 2: adjust background color
		rootView.setBackgroundColor(hasChildren && !isExpanded ? GROUP_BACKGROUND : Color.TRANSPARENT);
	}

	String joinChildValues(Field f) {
		StringBuilder builder = new StringBuilder();
		for (Field cf : f.childFields()) {
			String v = cf.getValue();
			if (v != null && !v.isEmpty()) {
				if (builder.length() > 0)
					builder.append(";");
				builder.append(v);
			}
		}
		return builder.toString();
	}

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final Field f = (Field) getGroup(groupPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }

		boolean hasChildren = f.childrenCount() > 0;
		adjustGroupView(convertView, isExpanded, hasChildren);

        String text = f.getId();

        CheckedTextView viewTag = (CheckedTextView) convertView.findViewById(R.id.tag);
        assert(viewTag != null);
        viewTag.setText(text);

        TextView viewValue = (TextView) convertView.findViewById(R.id.value);
		String value = f.getValue();
		if (!isExpanded && (value == null || value.isEmpty())) {
			value = joinChildValues(f);
		}
        viewValue.setText(value);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Field f = (Field) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.tag);
        text.setText(f.getId());

        text = (TextView) convertView.findViewById(R.id.value);
        text.setText(f.getValue());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, f.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
