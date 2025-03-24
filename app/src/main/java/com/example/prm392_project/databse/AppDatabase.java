package com.example.prm392_project.databse;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.prm392_project.bean.Cart;
import com.example.prm392_project.bean.Category;
import com.example.prm392_project.bean.Order;
import com.example.prm392_project.bean.OrderDetail;
import com.example.prm392_project.bean.Product;
import com.example.prm392_project.bean.Review;
import com.example.prm392_project.bean.User;
import com.example.prm392_project.dao.CartDao;
import com.example.prm392_project.dao.CategoryDao;
import com.example.prm392_project.dao.OrderDao;
import com.example.prm392_project.dao.OrderDetailDao;
import com.example.prm392_project.dao.ProductDao;
import com.example.prm392_project.dao.ReviewDao;
import com.example.prm392_project.dao.UserDao;
import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Review.class, Cart.class, OrderDetail.class, Product.class, Category.class, Order.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase{
    public abstract UserDao userDao();
    public abstract ReviewDao reviewDao();
    public abstract CartDao cartDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract OrderDao orderDao();
        private static AppDatabase INSTANCE = null;

    // ExecutorService để chạy các tác vụ khởi tạo dữ liệu trong background
    private static final ExecutorService dbExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "shopping_marketDB")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Gọi hàm populateDatabase khi database được tạo lần đầu
                                    dbExecutor.execute(() -> populateDatabase(INSTANCE));
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Khởi tạo dữ liệu mẫu khi cài đặt ứng dụng lần đầu
    private static void populateDatabase(AppDatabase db) {
        if (db == null) return;

        // Khởi tạo user va admin
        User user = new User("Cao Duy Quan", "0987654322",
                "user3@gmail.com", "Huyen Thach That, Xa Thach Hoa",
               "Male", BCrypt.hashpw("Abc123456@",BCrypt.gensalt()),true,false);
        db.userDao().insert(user);
        User admin = new User("Nguyen Van A", "0987654329",
                "user2@gmail.com", "Huyen Thach That, Xa Thach Hoa",
                "Male",BCrypt.hashpw("Abc123456@",BCrypt.gensalt()),true,true);
        db.userDao().insert(admin);

        // Khởi tạo các danh mục sản phẩm
        Category[] categories = new Category[] {
                new Category("Food", true),
                new Category("Drink", true),
                new Category("Electronics", true),
                new Category("Fashion", true),
                new Category("Home & Living", true),
                new Category("Health & Beauty", true)
        };

        for (Category category : categories) {
            db.categoryDao().insert(category);
        }

        // Khởi tạo các sản phẩm mẫu
        Product[] products = new Product[] {
                // Food (CategoryID: 1)
                new Product("Gạo ST25", "Gạo ST25 cao cấp từ Đồng bằng sông Cửu Long, đã đạt giải gạo ngon nhất thế giới. Hạt gạo dài, trắng, thơm và dẻo khi nấu. Đặc biệt giữ nguyên hương vị sau khi nấu, không bị cứng khi để nguội, phù hợp cho các bữa cơm gia đình hoặc nhà hàng cao cấp.", 95000,
                        "https://5rice.vn/wp-content/uploads/2018/05/ST25-XANH-copy.jpg", getTodayDate(), getTodayDate(), true, 1, 50, true),
                new Product("Mì Hảo Hảo", "Mì Hảo Hảo vị tôm chua cay, sản phẩm quen thuộc của mọi gia đình Việt Nam. Sợi mì vàng dai, nước súp đậm đà với vị tôm chua cay đặc trưng. Sản phẩm được chế biến trong vòng 3 phút, phù hợp cho bữa ăn nhanh, tiện lợi khi đi du lịch hoặc những lúc không có thời gian nấu nướng.", 3500,
                        "https://acecookvietnam.vn/wp-content/uploads/2017/07/BAG-HAO-HAO-TCC.png", getTodayDate(), getTodayDate(), true, 1, 200, false),
                new Product("Bánh Đậu Xanh Hải Dương", "Bánh đậu xanh Hải Dương là đặc sản nổi tiếng của vùng đất Bắc Bộ, được làm từ đậu xanh loại 1 và các nguyên liệu tự nhiên. Bánh có vị ngọt thanh, béo bùi từ đậu xanh và mỡ lợn tinh luyện. Mỗi chiếc bánh được gói cẩn thận trong giấy bóng kính với họa tiết truyền thống, thích hợp làm quà biếu hoặc thưởng thức cùng trà nóng.", 65000,
                        "https://dacsanthanhphuong.vn/wp-content/uploads/2022/12/banh-dau-xanh-hoa-an-9.jpeg", getTodayDate(), getTodayDate(), true, 1, 80, true),
                new Product("Nước Mắm Phú Quốc", "Nước mắm Phú Quốc được sản xuất từ cá cơm tươi đánh bắt tại vùng biển Phú Quốc, ủ chượp truyền thống trong thùng gỗ ít nhất 12 tháng. Sản phẩm có màu nâu cánh gián đẹp mắt, mùi thơm đặc trưng và vị đậm đà. Với hàm lượng đạm cao 35-40N, nước mắm Phú Quốc là gia vị không thể thiếu trong ẩm thực Việt Nam.", 60000,
                        "https://phuquocxanh.com/vi/wp-content/uploads/2017/02/N%C6%B0%E1%BB%9Bc-m%E1%BA%AFm-Ph%C3%BA-Qu%E1%BB%91c.jpg", getTodayDate(), getTodayDate(), true, 1, 70, false),
                new Product("Chả Lụa Vissan", "Chả lụa Vissan được sản xuất từ thịt heo tươi ngon, đảm bảo vệ sinh an toàn thực phẩm. Sản phẩm có màu trắng ngà tự nhiên, mùi thơm đặc trưng của thịt heo và lá chuối. Dai, giòn và ngọt thịt khi ăn. Được gói cẩn thận trong lá chuối tươi giúp giữ hương vị đặc trưng. Phù hợp cho bữa sáng với bánh mì, bún, xôi hoặc làm nguyên liệu cho nhiều món ăn khác.", 45000,
                        "https://vissanmart.com/pub/media/catalog/product/cache/ee97423e9fa68a0b8b7aae16fe28a6ff/g/i/gio_lua_db_1.png", getTodayDate(), getTodayDate(), true, 1, 60, true),

                // Beverages (CategoryID: 2)
                new Product("Cà Phê Trung Nguyên", "Cà phê Trung Nguyên được chọn lọc từ những hạt cà phê Robusta và Arabica ngon nhất vùng Tây Nguyên. Rang xay với công thức độc đáo tạo nên hương thơm đặc trưng, vị đắng đậm đà và hậu vị ngọt thanh kéo dài. Đặc biệt thích hợp pha theo phương pháp truyền thống với phin giọt Việt Nam. Tạo nên ly cà phê sánh đặc, thơm nồng, đánh thức mọi giác quan buổi sáng.", 75000,
                        "https://trungnguyencoffeevn.com/wp-content/uploads/2018/09/G7-Instant-Black-Coffee-no-sugar-317f.jpg", getTodayDate(), getTodayDate(), true, 2, 150, false),
                new Product("Nước Yến Sào", "Nước yến sào được chế biến từ tổ yến thiên nhiên, thu hoạch tại các hang động ven biển miền Trung Việt Nam. Sản phẩm giàu protein, axit amin và khoáng chất thiết yếu cho cơ thể. Được nấu với đường phèn tự nhiên tạo vị ngọt thanh, không gây ngán. Thích hợp sử dụng hàng ngày để tăng cường sức khỏe, đẹp da và phòng ngừa lão hóa.", 45000,
                        "https://media.loveitopcdn.com/1082/134203-dsc00179-ok-iso-small.jpg", getTodayDate(), getTodayDate(), true, 2, 100, true),
                new Product("Trà Ô Long TH True Tea", "Trà Ô Long TH True Tea được làm từ lá trà Ô Long hữu cơ từ vùng cao nguyên Mộc Châu. Sản phẩm đã qua quá trình lên men một phần tạo nên màu vàng hổ phách đẹp mắt và hương thơm tự nhiên đặc trưng. Vị trà thanh mát, hậu ngọt, không chát. Đóng chai tiện lợi, dễ dàng sử dụng mọi lúc mọi nơi. Không chứa chất bảo quản, tốt cho sức kh��e người tiêu dùng.", 10000,
                        "https://batos.vn/images/products/2023/08/16/410055531-1-346.jpg", getTodayDate(), getTodayDate(), true, 2, 120, false),
                new Product("Bia Saigon Special", "Bia Saigon Special được sản xuất từ các nguyên liệu nhập khẩu chất lượng cao: hoa bia Hallertau từ Đức, malt đại mạch từ Úc và men bia từ Hà Lan. Với công nghệ lên men lạnh hiện đại tạo nên hương vị đặc trưng: vị đắng nhẹ của hoa bia, hậu vị êm dịu và hương thơm tinh tế. Độ cồn 4.9%, thích hợp cho các bữa tiệc, liên hoan hoặc giải khát những ngày hè nóng bức.", 15000,
                        "https://iwater.vn/Image/Picture/Bia/saigon-special-lon-330ml.png", getTodayDate(), getTodayDate(), true, 2, 200, true),
                new Product("Nước Giải Khát Coca Cola", "Coca Cola là thức uống giải khát có ga nổi tiếng toàn cầu với hương vị độc đáo không th��� nhầm lẫn. Sản phẩm được đóng lon nhôm 330ml tiện lợi, dễ dàng bảo quản và sử dụng. Nước đen sánh với bọt ga mạnh mẽ, hương thơm đặc trưng và vị ngọt sảng khoái. Thích hợp uống lạnh với đá trong những ngày nắng nóng hoặc dùng làm nguyên liệu pha chế nhiều loại đồ uống khác.", 9500,
                        "https://cdn.tgdd.vn/Products/Images/2443/87880/bhx/thung-24-lon-nuoc-ngot-coca-cola-320ml-202304131109287672.jpg", getTodayDate(), getTodayDate(), true, 2, 250, false),

                // Electronics (CategoryID: 3)
                new Product("Nồi Cơm Điện Sharp", "Nồi cơm điện Sharp với thiết kế hiện đại, công suất 700W và dung tích 1.8 lít, phù hợp cho gia đình 4-6 người. Lòng nồi dày phủ lớp chống dính cao cấp, giúp cơm chín đều và không bị dính. Trang bị hệ thống điều khiển điện tử thông minh với nhiều chế độ nấu như: cơm thường, cơm mềm, cơm nhanh và giữ ấm tự động lên đến 12 giờ. Mặt ngoài bằng thép không gỉ dễ lau chùi, bền đẹp theo thời gian.", 950000,
                        "https://kinghouse.vn/wp-content/ckfinder/images/Noi-com-dien-Sharp-D40V-2.jpg", getTodayDate(), getTodayDate(), true, 3, 30, true),
                new Product("Bàn Ủi Philips", "Bàn ủi hơi nước Philips với công nghệ chống vôi hóa Anti-Calc giúp kéo dài tuổi thọ sản phẩm. Mặt đế SteamGlide bằng gốm cao cấp trượt nhẹ nhàng trên mọi loại vải. Công suất 2400W mạnh mẽ cùng hơi phun mạnh liên tục 45g/phút giúp loại bỏ nếp nhăn khó ủi. Tính năng phun hơi tăng cường lên đến 180g/phút xử lý hiệu quả các nếp nhăn cứng đầu. Thiết kế đầu ủi nhọn tiện lợi cho các góc khó và chi tiết nhỏ trên quần áo.", 750000,
                        "https://cdn11.dienmaycholon.vn/filewebdmclnew/public/userupload/files/Mtsp/gia-dung/philips-dst0520-20-so-huu-thiet-ke-dau-ban-ui-nhon-doc-dao.jpg", getTodayDate(), getTodayDate(), true, 3, 25, false),
                new Product("Máy Xay Sinh Tố Sunhouse", "Máy xay sinh tố Sunhouse đa năng với công suất 400W mạnh mẽ xay nhuyễn mọi loại trái cây, rau củ, đá viên. Bộ lưỡi dao 6 cánh bằng thép không gỉ 304 sắc bén, bền bỉ theo thời gian. Cối xay 1.5 lít làm từ nhựa PC cao cấp chịu lực tốt, không chứa BPA, an toàn cho sức khỏe. Điều chỉnh 2 tốc độ và chế độ nhồi (Pulse) linh hoạt theo nhu cầu sử dụng. Hệ thống chống rò rỉ và khóa an toàn khi vận hành.", 650000,
                        "https://cdn.tgdd.vn/Products/Images/1985/67697/sunhouse-shd-5322-thumb.jpg", getTodayDate(), getTodayDate(), true, 3, 20, true),
                new Product("Đèn LED Rạng Đông", "Đèn LED Rạng Đông công suất 12W thay thế hoàn hảo cho bóng đèn sợi đốt 100W, tiết kiệm đến 90% điện năng. Ánh sáng trắng 6500K rõ ràng, không nhấp nháy, bảo vệ thị lực người dùng. Tuổi thọ lên đến 20.000 giờ, tương đương 10 năm sử dụng (với 5-6 giờ/ngày). Thiết kế nhỏ gọn, vỏ nhựa chống cháy chịu nhiệt tốt. Khởi động tức thì, không phát tia UV có hại cho da, thân thiện với môi trường.", 35000,
                        "https://anlacphat.com/wp-content/uploads/2018/10/bong-den-downlight-12w-rang-dong-PT03-160-12W-lo-khoet-160-gia-canh-tranh.jpg", getTodayDate(), getTodayDate(), true, 3, 100, false),
                new Product("Quạt Điện Senko", "Quạt điện Senko L1638 v���i thiết kế hiện đại, tiết kiệm không gian. Động cơ đồng 100% bền bỉ, vận hành êm ái với công suất 55W. Ba mức tốc độ điều chỉnh linh hoạt theo nhu cầu sử dụng. Cánh quạt 16 inch làm từ nhựa PP cao cấp, bền đẹp và an toàn cho người dùng. Chế độ xoay 90 độ tự động phân phối gió đều khắp phòng. Tính năng hẹn giờ từ 1-8 tiếng tiết kiệm điện vào ban đêm.", 450000,
                        "https://cdn.tgdd.vn/Products/Images/1992/268452/lung-senko-l1638-290823-034304-600x600.jpg", getTodayDate(), getTodayDate(), true, 3, 40, true),

                // Fashion (CategoryID: 4)
                new Product("Áo Thun Cotton Uniqlo", "Áo thun nam Uniqlo làm từ 100% cotton Supima cao cấp, mềm mại và thấm hút tốt. Áo cổ tròn phom Regular Fit thoải mái, phù hợp với mọi vóc dáng. Đường may tỉ mỉ, chắc chắn với công nghệ xử lý vải chống co rút khi giặt. Màu sắc đa dạng, dễ dàng phối với nhiều loại trang phục khác nhau. Thiết kế tối giản, lịch sự phù hợp cho cả đi làm, đi chơi hay ở nhà.", 250000,
                        "https://down-vn.img.susercontent.com/file/726623675382b001407d14477625fee1", getTodayDate(), getTodayDate(), true, 4, 70, false),
                new Product("Tất Vớ Nam Coolmate", "Bộ 5 đôi tất nam Coolmate được làm từ 80% sợi cotton và 20% spandex co giãn tốt. Thiết kế cổ trung (Crew Length) phù hợp với giày tây, giày thể thao. Phần gót và mũi tất được dệt dày hơn tăng độ bền và tho���i mái khi đi cả ngày. Viền chun co giãn vừa phải, không gây tức hay hằn chân. Màu đen, xám, navy dễ phối với nhiều loại trang phục và giày dép khác nhau.", 120000,
                        "https://media3.coolmate.me/cdn-cgi/image/width=672,height=990,quality=80,format=auto/uploads/June2023/2D-co_dai_anti_truot-2.jpg", getTodayDate(), getTodayDate(), true, 4, 90, true),
                new Product("Khẩu Trang Vải Kháng Khuẩn", "Khẩu trang vải kháng khuẩn cao cấp với 3 lớp bảo vệ hiệu quả: lớp ngoài chống nước, lớp giữa kháng khuẩn và lớp trong mềm mại, thân thiện với da. Công nghệ nano bạc kháng khuẩn được chứng nhận giúp tiêu diệt vi khuẩn đến 99.9%. Thiết kế ôm sát khuôn mặt với gọng mũi có thể uốn cong linh hoạt. Dây đeo tai co giãn êm ái, không gây đau khi đeo lâu. Có thể giặt và tái sử dụng lên đến 30 lần, thân thiện với môi trường.", 35000,
                        "https://shop.nagakawa.com.vn/media/product/233_box_khau_trang.jpg", getTodayDate(), getTodayDate(), true, 4, 150, false),
                new Product("Mũ Nón Kết Biti's", "Mũ lưỡi trai Biti's được làm từ vải cotton cao cấp thoáng khí, thấm hút mồ hôi tốt. Thiết kế 6 panel cổ điển với logo thêu nổi tinh tế phía trước. Phần lưỡi trai cong vừa phải, giúp che nắng hiệu quả. Dây điều chỉnh phía sau với khóa kim loại bền bỉ, dễ dàng điều chỉnh kích thước phù hợp với mọi người. Màu đen trang nhã dễ dàng kết hợp với nhiều phong cách trang phục khác nhau.", 85000,
                        "https://product.hstatic.net/1000230642/product/non_den_e4ce3ea09c8845268d57ef9c5524a115_master.jpg", getTodayDate(), getTodayDate(), true, 4, 60, true),
                new Product("Dép Nhựa Thái Lan", "Dép nhựa Thái Lan với thiết kế đơn giản, bền bỉ từ chất liệu EVA cao cấp, nhẹ và mềm hơn nhựa thông thường. Đế dép có độ đàn hồi tốt, êm chân khi di chuyển. Rãnh chống trượt giúp an toàn trên mọi bề mặt, đặc biệt là sàn nhà tắm ướt. Màu sắc tươi sáng không phai màu sau thời gian sử dụng. Thiết kế chống thấm nước, dễ dàng vệ sinh, thích hợp sử dụng trong nhà tắm, bể bơi hay đi biển.", 45000,
                        "https://bachvietachau.com/wp-content/uploads/2017/08/dep-moi-Thai-Lan-cho-nu-mau-vang-xanh-duong-2.jpg", getTodayDate(), getTodayDate(), true, 4, 80, false),

                // Home & Living (CategoryID: 5)
                new Product("Chổi Lau Nhà Lock&Lock", "Chổi lau nhà phẳng Lock&Lock với thiết kế hiện đại, đầu lau xoay 180 độ linh hoạt vào mọi góc nhà. Bề mặt lau làm từ sợi microfiber cao cấp, hút bụi và nước hiệu quả, không để lại vệt trên sàn. Cán chổi bằng inox không gỉ, có thể điều chỉnh chiều dài từ 90-120cm phù hợp với mọi chiều cao người sử dụng. Đầu lau có thể tháo rời dễ dàng vệ sinh sau khi sử dụng. Phù hợp với nhiều loại sàn như: gỗ, gạch men, đá hoa cương...", 195000,
                        "https://locknlockvietnam.com/wp-content/uploads/2021/11/HPP345S2-2.jpg", getTodayDate(), getTodayDate(), true, 5, 45, true),
                new Product("Bộ Hộp Nhựa Đựng Thực Phẩm", "Bộ 3 hộp đựng thực phẩm với dung tích đa dạng 500ml, 1000ml và 2000ml, đáp ứng mọi nhu cầu bảo quản. Làm từ nhựa PP cao cấp trong suốt, không chứa BPA, an toàn cho sức khỏe. Nắp hộp với 4 khóa bảo đảm độ kín tuyệt đối, không rò rỉ chất lỏng. Thiết kế chịu nhiệt từ -20°C đến 120°C, có thể sử dụng trong lò vi sóng, tủ lạnh và máy rửa bát. Xếp chồng gọn gàng, tiết kiệm không gian bảo quản khi không sử dụng.", 125000,
                        "https://cdn.tgdd.vn/Products/Images/4929/327251/bo-3-hop-dung-thuc-pham-nhua-500-1000-2000-ml-inochi-hokkaido-hin-hocn-bo02-0-600x600.jpg", getTodayDate(), getTodayDate(), true, 5, 60, false),
                new Product("Bọt Biển Rửa Chén", "Bộ 3 miếng bọt biển rửa chén với thiết kế hai mặt đa năng: mặt xốp mềm thấm nước tốt để làm sạch bề mặt thông thường và mặt nhám với sợi nilon giúp cọ rửa vết bẩn cứng đầu. Kích thước 11x7x3cm vừa vặn trong lòng bàn tay, dễ dàng cầm nắm và sử dụng. Vật liệu cao cấp không hấp thụ mùi, kháng khuẩn, nhanh khô sau khi sử dụng. Màu sắc tươi sáng giúp dễ dàng nhận biết trong bồn rửa chén.", 25000,
                        "https://salt.tikicdn.com/cache/w300/ts/product/19/84/60/10323edfe7ad5fbd2a9c02fc3b02afd0.jpg", getTodayDate(), getTodayDate(), true, 5, 100, true),
                new Product("Màn Chống Muỗi", "Màn chống muỗi cao cấp làm từ sợi polyester mịn với mắt lưới nhỏ 30 lỗ/cm2, ngăn chặn hiệu quả muỗi và các loại côn trùng nhỏ. Kích thước 1m9 x 2m x 1m9 phù hợp với giường đôi. Cửa màn thiết kế dạng khóa kéo hai chiều tiện lợi khi ra vào. Khung treo dạng ô vuông treo bằng móc trần, dễ dàng lắp đặt và tháo gỡ. Vải mềm mại, thoáng khí không gây cảm giác bí bách khi sử dụng trong mùa hè.", 150000,
                        "https://down-vn.img.susercontent.com/file/f68d019ec0452529e9d927959a3ef9b8", getTodayDate(), getTodayDate(), true, 5, 40, false),
                new Product("Nến Thơm Phòng", "Nến thơm cao cấp làm từ sáp đậu nành tự nhiên, thân thiện với môi trường, không khói đen và mùi hóa chất khi đốt. Hương thơm tinh dầu thiên nhiên với bốn lựa chọn: hoa nhài, lavender, quế cam và gỗ đàn hương, lưu hương đến 40 giờ. Thiết kế cốc thủy tinh trong suốt với nắp gỗ tự nhiên, vừa là đồ trang trí nội thất sang trọng. Thời gian cháy liên tục 30-35 giờ. Lý tưởng để thư giãn, giảm stress sau ngày làm việc mệt mỏi.", 75000,
                        "https://mall.kayla.vn/wp-content/uploads/2023/06/Thiet-ke-chua-co-ten-5.png", getTodayDate(), getTodayDate(), true, 5, 70, true),

                // Health & Beauty (CategoryID: 6)
                new Product("Dầu Gội X-Men", "Dầu gội X-Men For Boss Wood hương gỗ sang trọng, dành riêng cho nam giới. Công thức 2X sạch sâu với than hoạt tính và muối biển, giúp làm sạch tóc và da đầu khỏi bụi bẩn, dầu nhờn hiệu quả. Chiết xuất bạc hà tạo cảm giác mát lạnh, sảng khoái khi gội. Vitamin B5 và dưỡng chất tự nhiên nuôi dưỡng tóc khỏe mạnh, óng mượt. Hương thơm nam tính, lịch lãm kéo dài suốt ngày dài.", 75000,
                        "https://concung.com/2022/04/56954-86860-large_mobile/dau-goi-nuoc-hoa-x-men-wood-2x-sach-sau-thom-lau-650g.jpg", getTodayDate(), getTodayDate(), true, 6, 80, false),
                new Product("Kem Đánh Răng P/S", "Kem đánh răng P/S với công nghệ bảo vệ toàn diện ProTect System, kết hợp các hoạt chất diệt khuẩn và Ion Fluoride giúp ngăn ngừa 99.9% vi khuẩn gây hại. Bảo vệ răng chắc khỏe suốt 12 giờ, ngăn ngừa 6 vấn đề về răng miệng: sâu răng, viêm lợi, cao răng, mảng bám, hôi miệng và ê buốt. Hương bạc hà the mát, tạo cảm giác sạch sẽ, thơm tho suốt ngày dài. Không chứa đường, an toàn cho cả gia đình sử dụng.", 25000,
                        "https://trungsoncare.com/images/detailed/12/1_8on6-lg.jpg", getTodayDate(), getTodayDate(), true, 6, 120, true),
                new Product("Sữa Tắm Dove", "Moisturizing body wash with dove cream", 90000,
                        "https://product.hstatic.net/1000006063/product/xanh_duong_610e1bdb6cd44d2c8c0de856557f6cd1_1024x1024.jpg", getTodayDate(), getTodayDate(), true, 6, 70, false),
                new Product("Lăn Khử Mùi Nivea", "Roll-on deodorant for 48-hour protection", 55000,
                        "https://www.guardian.com.vn/media/catalog/product/cache/30b2b44eba57cd45fd3ef9287600968e/b/0/b016fa5889de60e37d05ec7bee8d45b5c023811e4c62a0f3454f513a6107957b.jpeg", getTodayDate(), getTodayDate(), true, 6, 60, true),
                new Product("Băng Vệ Sinh Diana", "Women's sanitary pads, pack of 10", 35000,
                        "https://product.hstatic.net/1000006063/product/diana_sensi_sieu_mong_canh__8_mieng_._c0b698ff71b84344a3679843a16ee464_1024x1024.jpg", getTodayDate(), getTodayDate(), true, 6, 90, false)
        };

        for (Product product : products) {
            db.productDao().insert(product);
        }
    }
    private static String getTodayDate() {
        return java.time.LocalDate.now().toString();
    }
}