import { FC } from "react";
import { NavLink, Outlet } from "react-router-dom"
import "../../styles/header.css"

const Header:FC = () => {
    return (
        <>
            <header>
            <div id="main-menu">
                <div className="menu-left">
                <NavLink className="menu-item" to="/">Home</NavLink>
                <NavLink className="menu-item"to="/users">Users</NavLink>
                </div>
                <div className="menu-right">
                <NavLink className="menu-item"to="/sign-in">Sign in</NavLink>
                <NavLink className="menu-item" to="/register">Register</NavLink>
                </div>
            </div>
            <div className="divider-div"></div>
            </header>

            <Outlet />
        </>
    )
}
export default Header;