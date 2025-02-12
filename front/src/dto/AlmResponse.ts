import {AlmResponseError} from "./AlmResponseError";

export interface AlmResponse<T> {
    uuid: string
    requestUuid?: string
    status: string;
    error?: AlmResponseError;
    timestamp?: Date;
    data?: T
}