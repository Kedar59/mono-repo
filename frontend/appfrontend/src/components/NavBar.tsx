import { Link } from "react-router-dom";

const NavBar: React.FC = () => {
    return (
        <nav className="bg-white shadow-md p-4 flex justify-between items-center">
            <div>
                <Link to="/login">
                    <button className="bg-blue-500 text-white px-4 py-2 rounded mr-2 hover:bg-blue-600">Login</button>
                </Link>
                <Link to="/register">
                    <button className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">Register</button>
                </Link>
            </div>
        </nav>
    );
};
export default NavBar;