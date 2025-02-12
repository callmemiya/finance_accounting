export class FinanceEventKey {
    static readonly values: FinanceEventKey[] = [];
    static readonly REPORTS = new FinanceEventKey("reports");
    static readonly SALARY = new FinanceEventKey("salary");
    static readonly CATEGORIES = new FinanceEventKey("categories");

    private constructor(readonly value: string) {
        FinanceEventKey.values.push(this);
    }

    static toArray(): string[] {
        return FinanceEventKey.values.map((type) => type.value);
    }

    static getByValue(value: string | undefined): FinanceEventKey | undefined {
        return this.values.find(
            (accountType: FinanceEventKey) => accountType.value === value
        );
    }

    to(): string {
        return `to=${this.value}`;
    }
}
