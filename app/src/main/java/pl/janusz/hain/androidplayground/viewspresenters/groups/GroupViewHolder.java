package pl.janusz.hain.androidplayground.viewspresenters.groups;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import pl.janusz.hain.androidplayground.R;

public class GroupViewHolder extends ParentViewHolder {

    private TextView textViewGroupTitle;

    public GroupViewHolder(View itemView) {
        super(itemView);
        textViewGroupTitle = (TextView) itemView.findViewById(R.id.groupTitle);
    }

    public void bind(GroupParentObject group) {
        textViewGroupTitle.setText(group.getGroup().getTitleOfGroup());
    }
}
