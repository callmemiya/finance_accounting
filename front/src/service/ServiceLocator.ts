/**
 * Реализация паттерна Service Locator Pattern
 * В будущем может быть заменен паттерном DI (реализация inversify)
 * Разница между Service Locator и Dependency Injection паттернами - https://stackoverflow.com/questions/1557781/whats-the-difference-between-the-dependency-injection-and-service-locator-patte
 *
 * Service Locator:
 * https://medium.com/@avinkavish/decoupling-your-typescript-modules-with-the-service-locator-pattern-1e9d6e8378ce
 *
 * Dependency Injection:
 * https://github.com/inversify/InversifyJS
 * https://itnext.io/dependency-injection-in-react-using-inversifyjs-a38ff0c6601
 * https://itnext.io/dependency-injection-in-react-using-inversifyjs-now-with-react-hooks-64f7f077cde6
 *
 * @see src/services/config/Configuration.ts
 */
export class ServiceLocator {

    static readonly Instance = new ServiceLocator();

    private _map = new Map<string, any>();

    private constructor() {}

    get<T>(key: string): T | undefined {
        return this._map.get(key);
    }

    add(key: string, instance: {}): ServiceLocator {
        this._map.set(key, instance);
        return this;
    }

    clear(): void {
        this._map.clear();
    }

    static get<T>(key: string): T | undefined {
        return this.Instance.get(key);
    }

    static add(key: string, instance: {}): ServiceLocator {
        return this.Instance.add(key, instance);
    }

    static clear(): void {
        this.Instance.clear();
    }

}
