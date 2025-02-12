import "./home.css";
import {Link} from "react-router-dom";
import React from "react";
import {homeItems} from "./home-items";
import {BasicContainer} from "../workspace/BasicContainer";

type ShowLinkProps = {
    show: boolean;
    linkUrl: string;
    children: React.ReactNode;
}

//Дополнительный элемент для отображения ссылки если элемент активирован
const ShowLink = ({show = true, linkUrl, children}: ShowLinkProps) => {
    if (show) {
        return (<Link className="home-link card-home" to={linkUrl}> {children} </Link>)
    } else {
        return (<div className="card-home justify-content-around">{children}</div>)
    }
}

type HomeCardProps = {
    linkUrl: string;
    name: string;
    imgUrl: string;
    showLink: boolean;
}

//Элемент для отображения карточки
const HomeCard = ({linkUrl, name, imgUrl, showLink = true}: HomeCardProps) => {
    return (
        <ShowLink show={showLink} linkUrl={linkUrl}>
            <div className="card w-100 h-100 border-0 flex-row">
                <table width="100%">
                    <thead>
                    <tr>
                        <th style={{width: "10%"}}>
                            <img className="card-img-home" src={imgUrl} alt="module_image"/>
                        </th>
                        <th>
                            <div className="card card-icon ms-3 border-0">
                                <div>
                                    {name}
                                </div>
                            </div>
                        </th>
                    </tr>
                    </thead>
                </table>
            </div>
        </ShowLink>
    )
}

const Home = () => {
    return <BasicContainer title="Разделы" classNameTitle="basic-container-title-main-page">
        <div className="home-container">
            {homeItems.map((item, idx) =>
                <div>
                    <HomeCard
                        name={item.name}
                        showLink={item.showLink}
                        linkUrl={item.linkUrl}
                        imgUrl={item.imgUrl}
                    />
                </div>
            )}
        </div>
    </BasicContainer>;
};

export default Home;
