import "./basic-containers.css"

type BasicContainerTitleProps = {
    title: string;
    className?: string;
}

export const BasicContainerTitle = (props: BasicContainerTitleProps) =>
    <h2 className={"basic-container-title ".concat(props?.className ?? "")}>{props.title}</h2>;
