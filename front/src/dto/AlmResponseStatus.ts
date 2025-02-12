/**
 * Тип операции по движению денежных средств
 */
export class AlmResponseStatus {

    static readonly values: AlmResponseStatus[] = [];

    static readonly SUCCESS = new AlmResponseStatus("SUCCESS");
    static readonly ERROR = new AlmResponseStatus("ERROR");
    static readonly BUSINESS_ERROR = new AlmResponseStatus("BUSINESS_ERROR");

    private constructor(readonly name: string) {
        AlmResponseStatus.values.push(this);
    }

    static getBy(name: string): AlmResponseStatus {
        return this.values.find((status) => status.name === name)!;
    }

}