export interface RouteConfig {
    path: string;
    name: string;
    enabled: boolean;
}

export const routesConfig: RouteConfig[] = [
    {
        path: "/",
        name: "Управление финансами",
        enabled: true,
    },
    {
        path: "/finance",
        name: "Финансы",
        enabled: true,
    },
    {
        path: "/forecast",
        name: "План-Прогноз",
        enabled: false,
    },
];
