import {type ReactNode} from 'react';
import styles from './alm-toolbar.module.css';
import classes from 'classnames';

type AlmToolbarProps = {
    children: ReactNode;
    className?: string;
}

export function AlmToolbar({children, className}: AlmToolbarProps) {
    return (
        <div className={classes(styles['alm-toolbar'], className ?? "")}>
            {children}
        </div>
    )
}