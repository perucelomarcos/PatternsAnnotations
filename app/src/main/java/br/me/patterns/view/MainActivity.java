package br.me.patterns.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import br.me.patterns.R;
import br.me.patterns.model.Carro;
import br.me.patterns.model.CarroBuilder_;
import br.me.patterns.model.MathSingleton;
import br.me.patterns.model.MathSingleton_;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScrollView scrollView = findViewById(R.id.scroll_view);
        TextView log = findViewById(R.id.log);

        findViewById(R.id.singleton_button).setOnClickListener(v -> {
            MathSingleton singletonObj = MathSingleton_.getInstance();

            int operationIndex = new Random().nextInt(4);
            int num1 = new Random().nextInt(1000);
            int num2 = new Random().nextInt(1000);
            float result = 0;
            String operation = "";

            switch (operationIndex) {
                case 0:
                    operation = "+";
                    result = singletonObj.sum(num1, num2);
                    break;
                case 1:
                    operation = "-";
                    result = singletonObj.subtract(num1, num2);
                    break;
                case 2:
                    operation = "*";
                    result = singletonObj.multiply(num1, num2);
                    break;
                case 3:
                    operation = "/";
                    result = singletonObj.divide(num1, num2);
                    break;
            }

            log.append("Testing singleton-----\n");
            log.append(String.format("%s %s %s = %s", num1, operation, num2, result));
            log.append("\n-----------------------------------\n\n");

            scrollView.postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 100);
        });

        findViewById(R.id.builder_button).setOnClickListener(v -> {
            boolean option1 = new Random().nextInt(2) == 1;

            CarroBuilder_ carBuilder = new CarroBuilder_()
                    .setAnoFabricacao(option1 ? 2013 : 2019)
                    .setArCondicionado(!option1)
                    .setArQuente(!option1)
                    .setCombustivel(option1 ? "Flex (gasolina ou álcool)": "Diesel")
                    .setCor(option1 ? "Preto" : "Prata")
                    .setMarca(option1 ? "Fiat" : "Ferrari")
                    .setQuilometragem(option1 ? "115.000km" : "15km")
                    .setRodasLigaLeve(true)
                    .setSensorEstacionamento(!option1);

            Carro carro = carBuilder.build();

            log.append("Testing builder-----\n");
            log.append(carro.toString());
            log.append("\n-----------------------------------\n\n");

            scrollView.postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 100);
        });
    }
}
