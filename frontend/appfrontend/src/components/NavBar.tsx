import { Link } from "react-router-dom";

const NavBar: React.FC = () => {
    return (
        <nav style={{ display: "flex", justifyContent: "space-between", padding: "10px", backgroundColor: "#f5f5f5", borderBottom: "1px solid #ccc" }}>
            <div>
                <Link to="/login">
                    <button style={{ marginRight: "10px" }}>Login</button>
                </Link>
                <Link to="/register">
                    <button>Register</button>
                </Link>
            </div>
        </nav>
    );
};
export default NavBar;