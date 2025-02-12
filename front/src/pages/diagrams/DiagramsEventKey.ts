export class DiagramsEventKey {
    static readonly values: DiagramsEventKey[] = [];
    static readonly DISCHARGE = new DiagramsEventKey("discharge");
    static readonly GAIN = new DiagramsEventKey("gain");

    private constructor(readonly value: string) {
        DiagramsEventKey.values.push(this);
    }

    static toArray(): string[] {
        return DiagramsEventKey.values.map((type) => type.value);
    }

    static getByValue(value: string | undefined): DiagramsEventKey | undefined {
        return this.values.find(
            (accountType: DiagramsEventKey) => accountType.value === value
        );
    }

    to(): string {
        return `to=${this.value}`;
    }
}
