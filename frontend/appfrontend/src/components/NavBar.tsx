import { Link } from "react-router-dom";
import { useAuth } from "./UseAuth";
const NavBar: React.FC = () => {
    const { isAuthenticated, logout } = useAuth();
    return (
        <nav className="bg-white shadow-md p-4 flex justify-between items-center">
            <div>
                {/* <Link to="/login">
                    <button className="bg-blue-500 text-white px-4 py-2 rounded mr-2 hover:bg-blue-600">Login</button>
                </Link>
                <Link to="/register">
                    <button className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">Register</button>
                </Link> */}
                {!isAuthenticated ? (
                    <>
                        <Link to="/login">
                            <button className="bg-blue-500 text-white px-4 py-2 rounded mr-2 hover:bg-blue-600">
                                Login
                            </button>
                        </Link>
                        <Link to="/register">
                            <button className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
                                Register
                            </button>
                        </Link>
                    </>
                ) : (
                    <>
                        <button
                            onClick={logout}
                            className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                            Logout
                        </button>
                        <Link to="/profile">
                            <button className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
                                Profile
                            </button>
                        </Link>
                        <Link to="/companies">
                            <button className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
                                Companies
                            </button>
                        </Link>
                    </>

                )}
            </div>
        </nav>
    );
};
export default NavBar;