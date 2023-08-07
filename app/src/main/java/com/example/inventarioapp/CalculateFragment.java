package com.example.inventarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CalculateFragment extends Fragment {
    private TextView tv_results, tv_operacion;

    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0,
            btn_porcentaje, btn_multi, btn_div, btn_rest, btn_igual, btn_sum,
            btn_punto, btn_c, btn_parentesis, btn_del, btn_cerrar_parentesis;

    private String data = "";
    private boolean decimal = false;
    private boolean tipoPar = false;
    private boolean bandera = false;

    public CalculateFragment() {
        // Required empty public constructor
    }

    public static CalculateFragment newInstance() {
        CalculateFragment fragment = new CalculateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculate, container, false);

        tv_operacion = view.findViewById(R.id.tv_operacion);
        tv_results = view.findViewById(R.id.tv_resultado);

        btn_0 = view.findViewById(R.id.btn_cero);
        btn_1 = view.findViewById(R.id.btn_uno);
        btn_2 = view.findViewById(R.id.btn_dos);
        btn_3 = view.findViewById(R.id.btn_tres);
        btn_4 = view.findViewById(R.id.btn_cuatro);
        btn_5 = view.findViewById(R.id.btn_cinco);
        btn_6 = view.findViewById(R.id.btn_seis);
        btn_7 = view.findViewById(R.id.btn_siete);
        btn_8 = view.findViewById(R.id.btn_ocho);
        btn_9 = view.findViewById(R.id.btn_nueve);

        btn_sum = view.findViewById(R.id.btn_mas);
        btn_rest = view.findViewById(R.id.btn_menos);
        btn_multi = view.findViewById(R.id.btn_multiplicar);
        btn_div = view.findViewById(R.id.btn_dividir);
        btn_c = view.findViewById(R.id.btn_c);
        btn_del = view.findViewById(R.id.btm_eliminar);
        btn_igual = view.findViewById(R.id.btn_igual);
        btn_punto = view.findViewById(R.id.btn_punto);
        btn_porcentaje = view.findViewById(R.id.btn_porcentaje);
        btn_parentesis = view.findViewById(R.id.btn_parentesis);
        btn_cerrar_parentesis = view.findViewById(R.id.btn_cerrar_parentesis);

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "0";
                tv_results.setText(data);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "1";
                tv_results.setText(data);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "2";
                tv_results.setText(data);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "3";
                tv_results.setText(data);
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "4";
                tv_results.setText(data);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "5";
                tv_results.setText(data);
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "6";
                tv_results.setText(data);
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "7";
                tv_results.setText(data);
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                data += "8";
                tv_results.setText(data);
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "9";
                tv_results.setText(data);
            }
        });

        btn_sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                        data += "+";
                        tv_results.setText(data);
                        decimal = false;
                    }
                }
            }
        });

        btn_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                        data += "-";
                        tv_results.setText(data);
                        decimal = false;
                    }
                }
            }
        });

        btn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                        data += "*";
                        tv_results.setText(data);
                        decimal = false;
                    }
                }
            }
        });

        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                        data += "/";
                        tv_results.setText(data);
                        decimal = false;
                    }
                }
            }
        });

        btn_punto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!decimal) {
                    if (data.length() == 0) {
                        data = "0.";
                    } else {
                        char lastChar = data.charAt(data.length() - 1);
                        if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/' && lastChar != '.') {
                            data += ".";
                        }
                    }
                    decimal = true;
                    tv_results.setText(data);
                }
            }
        });

        btn_porcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/' && lastChar != '.') {
                        data += "%";
                        tv_results.setText(data);
                        decimal = false;
                    }
                }
            }
        });

        btn_parentesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "(";
                tv_results.setText(data);
                /*if (data.length() == 0 || tipoPar) {
                    data += "(";
                    tv_results.setText(data);
                    tipoPar = false;
                    decimal = false;
                } else {
                    data += ")";
                    tv_results.setText(data);
                    tipoPar = true;
                    decimal = false;
                }*/
            }
        });

        btn_cerrar_parentesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += ")";
                tv_results.setText(data);
            }
        });

        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = "";
                decimal = false;
                tv_results.setText(data);
                tv_operacion.setText("");
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    String newData = data.substring(0, data.length() - 1);
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar == '.') {
                        decimal = false;
                    }
                    data = newData;
                    tv_results.setText(data);
                }
            }
        });

        btn_porcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                        data += "%";
                        tv_results.setText(data);
                        decimal = false;
                    }
                }
            }
        });

        btn_parentesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += "(";
                tv_results.setText(data);
                tipoPar = true;
                /*if (!tipoPar) {
                    data += "(";
                    tv_results.setText(data);
                    tipoPar = true;
                } else {
                    data += ")";
                    tv_results.setText(data);
                    tipoPar = false;
                }*/
            }
        });

        btn_cerrar_parentesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data += ")";
                tv_results.setText(data);
                tipoPar = false;

            }
        });

        btn_igual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.length() > 0) {
                    char lastChar = data.charAt(data.length() - 1);
                    if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                        data = data.substring(0, data.length() - 1);
                    }
                    String resultado = evaluar(data);
                    tv_operacion.setText(data);
                    tv_results.setText(resultado);
                    data = resultado;
                    bandera = true;
                }
            }
        });
        return view;
    }

    private String evaluar(String expresion) {
        String resultado = "";
        try {
            Expression expression = new ExpressionBuilder(expresion).build();
            double valor = expression.evaluate();
            resultado = Double.toString(valor);
            if (resultado.endsWith(".0")) {
                resultado = resultado.substring(0, resultado.length() - 2);
            }
        } catch (Exception e) {
            resultado = "Error";
        }
        return resultado;
    }
}

