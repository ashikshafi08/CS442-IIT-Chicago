package com.christopherhield.tablayoutexample;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentThree extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_three, container, false);

        TextView infoContent = v.findViewById(R.id.contentText);
        infoContent.setMovementMethod(new ScrollingMovementMethod());
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " + "" +
                "labore et dolore magna aliqua. Egestas sed tempus urna et. Nisi vitae suscipit tellus mauris a diam maecenas " +
                "sed enim.\n\nMi sit amet mauris commodo quis imperdiet massa tincidunt nunc. Eleifend donec pretium vulputate " +
                "sapien nec sagittis aliquam. Euismod in pellentesque massa placerat duis. Eu feugiat pretium nibh ipsum.\n\n" +
                "Lobortis mattis aliquam faucibus purus in massa. Et pharetra pharetra massa massa ultricies mi. Interdum " +
                "consectetur libero id faucibus nisl tincidunt eget nullam non. Arcu dictum varius duis at consectetur. " +
                "Euismod lacinia at quis risus sed vulputate.\n\nEu nisl nunc mi ipsum faucibus vitae aliquet nec ullamcorper. " + "" +
                "Amet dictum sit amet justo donec enim.\n\nSit amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar. " +
                "At risus viverra adipiscing at in tellus integer feugiat scelerisque. Lorem ipsum dolor sit amet consectetur " +
                "adipiscing elit duis tristique. Commodo viverra maecenas accumsan lacus vel facilisis volutpat est velit.\n\n" +
                "Dui vivamus arcu felis bibendum ut tristique et egestas.\n\n";
        infoContent.setText(content);

        return v;
    }
}
