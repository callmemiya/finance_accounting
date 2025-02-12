import React from "react";

const NoPage: React.FC = () => {
    const centerStyle: React.CSSProperties = {
        textAlign: "center",
        padding: "1rem 0"
    };

    return (
        <main style={centerStyle}>
            <h2> Страница не найдена </h2>
        </main>
    );
};

export default NoPage;
