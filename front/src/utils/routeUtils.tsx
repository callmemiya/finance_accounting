import {Dispatch, SetStateAction, useCallback, useEffect, useState} from "react";
import {useLocation, useSearchParams} from "react-router-dom";
import {TinyEmitter} from "tiny-emitter";

export const useHeaderRoutingHandler = (defaultKeyValue: string, rootPath: string): [string, (selectedKey: string | null) => void, (to: string) => string | undefined] => {

    const [key, setKey] = useState<string>(defaultKeyValue);
    const [searchParams, setSearchParams] = useSearchParams();
    const location = useLocation();

    useEffect(() => {
        const to: string | null = searchParams.get("to");
        !!to && setKey(to)
    },[searchParams])

    const resolveHref = useCallback((to: string): string | undefined =>
        location.pathname.endsWith(rootPath) || location.pathname.endsWith(`${rootPath}/`)
            ? undefined
            : `${rootPath}?${to}`, [location])

    const handleOnSelect = (selectedKey: string | null): void => {
        setKey(selectedKey!)
        selectedKey && emitter.emit('SECTION_TAB_SELECTED', selectedKey)
    }

    return [key, handleOnSelect, resolveHref]
}

/**
 * Общая шина событий в рамках фронта
 */
const emitter = new TinyEmitter();

export const useSectionTabHandler = (isValidKeyValue: (keyValue?: string) => boolean): [string | undefined, Dispatch<SetStateAction<string | undefined>>] => {
    const [key, setKey] = useState<string>();
    const [searchParams, setSearchParams] = useSearchParams();

    const callback = useCallback(() => {
        emitter.on('SECTION_TAB_SELECTED', (eventKey: string) => {
            !!eventKey && eventKey !== key && setKey(eventKey)
        })
    }, [])

    useEffect(() => {
        callback()
    }, [callback])

    useEffect(() => {
        const to = searchParams.get("to");
        if (!!to && isValidKeyValue(to)) {
            setKey(to)
            searchParams.delete("to");
            setSearchParams(searchParams);
        }
    }, [])
    return [key, setKey]
}