package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.icu.math.BigDecimal;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.annotation.IntegerRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class CalculatorActivity extends Activity {

    TextView text = null;
    int textState = 1; // 0 -- add; 1 -- substitute
    double prev = 0;
    int prevOperation = 0;
    Boolean isFloat = false;
    Boolean needToCount = false;
    Boolean canPrevCount = false;

    int[] keys = new int[10];
    View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.add || id == R.id.sub || id == R.id.mul || id == R.id.div || id == R.id.addsub || id == R.id.percent) {
                textState = 1;
                arithmeticButtonListener(v);
            }
            else if (id == R.id.equals || id == R.id.clear) {
                textState = 1;
                nonArithmeticButtonListener(v);
            }
            else numberListener(v);
        }
    };

    void numberListener(View v) {
        needToCount = true;
        if (v.getId() == R.id.dot) {
            if (!isFloat) {
                isFloat = true;
                textState = 0;
                changeText(".");
            }
        } else {
            int cur = 0;
            for (int i = 0; i < 10; ++i) {
                if (keys[i] == v.getId()) cur = i;
            }
            if (textState == 1) prev = Double.parseDouble(text.getText().toString());
            changeText(Integer.toString(cur));
        }
    }

    void arithmeticButtonListener(View v) {
        doCount();
        isFloat = false;
        if (v.getId() == R.id.add) {
            prevOperation = R.id.add;
        } else if (v.getId() == R.id.sub) {
            prevOperation = R.id.sub;
        } else if (v.getId() == R.id.mul) {
            prevOperation = R.id.mul;
        } else if (v.getId() == R.id.div) {
            prevOperation = R.id.div;
        } else if (v.getId() == R.id.addsub) {
            prevOperation = R.id.addsub;
            needToCount = true;
            canPrevCount = true;
            doCount();
        } else if (v.getId() == R.id.percent) {
            prevOperation = R.id.percent;
            needToCount = true;
            canPrevCount = true;
            doCount();
        }
    }

    void doCount() {
        if (needToCount && canPrevCount) {
            if (prevOperation == R.id.add) {
                prev += Double.parseDouble(text.getText().toString());
            } else if (prevOperation == R.id.sub) {
                prev -= Double.parseDouble(text.getText().toString());
            } else if (prevOperation == R.id.mul) {
                prev *= Double.parseDouble(text.getText().toString());
            } else if (prevOperation == R.id.div) {
                prev /= Double.parseDouble(text.getText().toString());
            } else if (prevOperation == R.id.addsub) {
                prev = -Double.parseDouble(text.getText().toString());
            } else if (prevOperation == R.id.percent) {
                prev = Double.parseDouble(text.getText().toString()) / 100.0;
            }
            textState = 1;
            changeText(Double.toString(prev));
            textState = 1;
        }
        needToCount = false;
        canPrevCount = true;
    }

    void nonArithmeticButtonListener(View v) {
        if (v.getId() == R.id.equals) {
            doCount();
            textState = 1;
            isFloat = true;
        } else {
            textState = 1;
            canPrevCount = false;
            prev = 0;
            changeText(Integer.toString(0));
        }
    }

    void changeText(String s) {
        if (textState == 0) {
            text.setText(text.getText().toString().concat(s));
        } else {
            text.setText(s);
            textState = 0;
            if (s.equals("0")) textState = 1;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        text = (TextView) findViewById(R.id.result);
        findViewById(R.id.equals).setOnClickListener(buttonListener);
        findViewById(R.id.add).setOnClickListener(buttonListener);
        findViewById(R.id.sub).setOnClickListener(buttonListener);
        findViewById(R.id.mul).setOnClickListener(buttonListener);
        findViewById(R.id.div).setOnClickListener(buttonListener);
        findViewById(R.id.addsub).setOnClickListener(buttonListener);
        findViewById(R.id.equals).setOnClickListener(buttonListener);
        findViewById(R.id.clear).setOnClickListener(buttonListener);
        findViewById(R.id.percent).setOnClickListener(buttonListener);
        findViewById(R.id.dot).setOnClickListener(buttonListener);
        keys[0] = R.id.d0;
        keys[1] = R.id.d1;
        keys[2] = R.id.d2;
        keys[3] = R.id.d3;
        keys[4] = R.id.d4;
        keys[5] = R.id.d5;
        keys[6] = R.id.d6;
        keys[7] = R.id.d7;
        keys[8] = R.id.d8;
        keys[9] = R.id.d9;
        for (int i = 0; i < 10; i++) {
            findViewById(keys[i]).setOnClickListener(buttonListener);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textState = savedInstanceState.getInt("textState");
        prev = savedInstanceState.getDouble("prev");
        prevOperation = savedInstanceState.getInt("prevOperation");
        isFloat = savedInstanceState.getBoolean("isFloat");
        needToCount = savedInstanceState.getBoolean("needToCount");
        canPrevCount = savedInstanceState.getBoolean("canPrevCount");
        text.setText(savedInstanceState.getString("saveText"));
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("textState", textState);
        outState.putDouble("prev", prev);
        outState.putInt("prevOperation", prevOperation);
        outState.putBoolean("isFloat", isFloat);
        outState.putBoolean("needToCount", needToCount);
        outState.putBoolean("canPrevCount", canPrevCount);
        outState.putString("saveText", text.getText().toString());
    }
}
