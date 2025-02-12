import notify from "devextreme/ui/notify";

/** Текущая тема (dx.material.orange.light) содержит стили для уведомлений без иконок. Чтобы это исправить,
 * для {@link notifyMessage} используются стили из файла devextreme/dist/css/dx.carmine.css
 * (относящиеся только к компоненту dx-toast). Добавленные и измененные значения стилей см. в комментариях в файле dx-toast.css */
// import "./dx-toast.css"

export namespace Notification {

    /** Тип уведомления */
    export class Type {

        static readonly values: Type[] = [];

        static readonly ERROR = new Type("error");
        static readonly WARNING = new Type("warning");
        static readonly INFO = new Type("info");
        static readonly SUCCESS = new Type("success");

        constructor(readonly value: string) {
            Type.values.push(this);
        }

        static getBy(value: string): Type {
            return this.values.find((type: Type) => type.value === value)!;
        }
    }

}

/** Значение времени отображения, чтобы уведомление не пропадало */
export const NO_DISPLAY_TIME = 1000000000;

/** Время отображения уведомления по умолчанию */
const DISPLAY_TIME_DEFAULT = 15000;

/** Мин. ширина уведомления */
const MIN_WIDTH = 50;
/** Мин. высота уведомления */
const MIN_HEIGHT = 50;

/**
 * Отображает всплывающее уведомление указанного типа с переданными сообщением.
 * <p>
 * В параметре {@link displayTime} устанавливается время отображения.
 * Чтобы уведомление не пропадало, необходимо передать значение {@link NO_DISPLAY_TIME}
 *
 * @param {string}            msg         текст уведомления
 * @param {Notification.Type} type        тип уведомления
 * @param {number}            displayTime время отображения уведомления (мс)
 *
 * @see https://js.devexpress.com/Documentation/Guide/UI_Components/Toast/Getting_Started_with_Toast
 */
export function notifyMessage(msg: string, type: Notification.Type = Notification.Type.ERROR,
                              displayTime: number = DISPLAY_TIME_DEFAULT) {

    const options = {
        minWidth: MIN_WIDTH,
        minHeight: MIN_HEIGHT,
        type: type.value,
        message: msg,
        shading: false,
        displayTime: displayTime,
        animation: {
            show: {
                type: 'fade', duration: 400, from: 0, to: 1,
            },
            hide: {
                type: 'fade', duration: 40, to: 0
            },
        }
    };

    const stack: any = {
        position: "bottom center",
        direction: 'up-push'
    };

    notify(options, stack);
}
