import React, {ReactNode} from "react";
import {Link} from "react-router-dom";

interface CustomLinkProps {
    enabled?: boolean;
    to?: string;
    children: ReactNode;
}

const CustomLink: React.FC<CustomLinkProps> = ({enabled = true, to = '#', children}) => {
    return enabled ? <Link to={to}>{children}</Link> : <>{children}</>;
};

export default CustomLink;
