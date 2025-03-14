    FirebaseAuth auth;

    private GoogleSignInClient mGoogleSignInClient;

    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        b1 = findViewById(R.id.text_sign_out);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                signOut();


            }
        });


    }

    private void signOut() {
        // Firebase sign out
        auth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // After signing out, redirect the user to the MainActivity
                        Intent intent = new Intent(HomePage.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(HomePage.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }
