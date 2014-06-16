package com.cb.test.ui;

import com.cb.R;
import com.cb.test.json.parser.ui.JsonParserActivity;
import com.cb.test.xml.parser.ui.XmlParserActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ParserActivity extends Activity
{
    private Button mXmlParserBtn, mJsonParserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);

        initViews();
    }

    private void initViews()
    {
        mXmlParserBtn = (Button) findViewById(R.id.xml_parser_btn);
        mJsonParserBtn = (Button) findViewById(R.id.json_parser_btn);

        mXmlParserBtn.setOnClickListener(listener);
        mJsonParserBtn.setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener()
    {

        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.xml_parser_btn:
                    startActivity(new Intent(ParserActivity.this, XmlParserActivity.class));
                    break;
                case R.id.json_parser_btn:
                    startActivity(new Intent(ParserActivity.this, JsonParserActivity.class));
                    break;
            }
        }
    };
}
