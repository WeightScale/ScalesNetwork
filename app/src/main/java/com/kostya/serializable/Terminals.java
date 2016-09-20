package com.kostya.serializable;

import android.util.SparseArray;

/**
 * @author Kostya  on 10.07.2016.
 */
public enum Terminals {
    XK3118T1 {
        /** Режим передачи данных. */
        private final SparseArray<Object> modeArray = new SparseArray<>();
        {   /** Режим по умолчанию. */
            modeArray.put(0, "");
            /** Пакет 8байт. Вес нетто в обратном порядке. */
            modeArray.put(1, "");
            /** Пакет 8байт. Вес бруто в обратном порядке. */
            modeArray.put(2, "");
            /** Пакет 14байт. Вес нетто и единица измерения например: 0023.45(kg) заканчивается символами CR,LF */
            modeArray.put(3, "");
            /** Пакет 14байт. Вес бруто и единица измерения например: 0023.45(kg) заканчивается символами CR,LF */
            modeArray.put(4, "");
            /** По запросу. Методом посылки символа то 'A' до 'Е'
             * “A”: Получить значення веса бруто: ответ “GW:0023.45(kg)”
             * “B”: Получить значення веса нетто: ответ “NW:0013.45(kg)”
             * “C”: Получить значення веса тари: ответ “TW:0010.00(kg)”
             * “D”: Установить на ноль: ответ “D”
             * “E”: Тарирование: ответ “E”
             * Строка начинается символом Hex 02 и оканчивается симовлом Hex 03 */
            modeArray.put(5, "");
            /** передаеться строка со значенням веса нето и сумарного веса.
             * Эта строка может быть разпечатана на принтере с последовательным интерфейсом. */
            modeArray.put(6, "");
        }

        @Override
        public String filter(String data) {
            return data;
        }
    },
    CI200A {
        @Override
        public String filter(String data) {
            return data;
        }
    },
    A12E{
        /** Формат данных   ww000.000kg (вес брутто)
         *                  wn000.000kg (вес нетто)
         *                  wt000.000kg (вес тара)
         * @param data Данные полученые с порта терминала.
         * @return Данные значения веса.
         */
        @Override
        public String filter(String data) {
            try {
                /** Парсим значение веса - ww000000 kg
                 * значение веса начинается с 2 заканчивается 8 символом. */
                String number = data.substring(2, 8);
                //String number = data.replaceAll("\\p{Alpha}|\\n|\\r","");
                /** Определяем значение с плавающей точкой. */
                if (number.contains(".")){
                    return String.valueOf(Double.parseDouble(number));
                }else {
                    return String.valueOf(Integer.parseInt(number,10));
                }
            }catch (NumberFormatException e){
                return "---";
            }
        }
    },
    DEFAULT {

        @Override
        public String filter(String data) {
            return data;
        }
    };

    /** Фильтр для подготовки предварительных данных.
     * @param data Данные полученые с порта терминала.
     * @return Возврат данных поготовленых для клиента.
     */
    public abstract String filter(String data);
}
