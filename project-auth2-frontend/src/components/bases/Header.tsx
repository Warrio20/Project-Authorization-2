import { FC, useContext } from "react";
import { NavLink, Outlet } from "react-router-dom"
import "../../styles/header.css"
import { DataContext } from "../../data/DataContext";

const Header:FC = () => {
    const data = useContext(DataContext);
    const loginPages = (<>
    <NavLink className="menu-item"to="/sign-in">Sign in</NavLink>
    <NavLink className="menu-item" to="/register">Register</NavLink>
    </>)
    return (
        <>
            <header>
            <div id="main-menu">
                <div className="menu-left">
                <NavLink className="menu-item" to="/">Home</NavLink>
                </div>
                <div className="menu-right">
                    {data?.isAuthorized ? data.user.username : loginPages}
                </div>
            </div>
            <div className="divider-div"></div>
            </header>
            <Outlet />
        </>
    )
}
export default Header;